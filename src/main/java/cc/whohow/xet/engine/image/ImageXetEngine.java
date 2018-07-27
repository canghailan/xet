package cc.whohow.xet.engine.image;

import cc.whohow.xet.AbstractXetEngine;
import cc.whohow.xet.layout.ComponentLayoutEngine;
import cc.whohow.xet.model.VirtualDOM;
import cc.whohow.xet.render.ComponentRenderEngine;
import cc.whohow.xet.render.RenderEngine;
import cc.whohow.xet.util.CacheableFactory;
import cc.whohow.xet.engine.awt.model.ColorFactory;
import cc.whohow.xet.engine.awt.model.FontFamilyFactory;
import cc.whohow.xet.engine.awt.model.ImageFactory;
import cc.whohow.xet.engine.awt.layout.AWTChineseTextLayoutEngine;
import cc.whohow.xet.engine.awt.layout.AWTImageLayoutEngine;
import cc.whohow.xet.engine.awt.render.AWTImageRenderEngine;
import cc.whohow.xet.engine.awt.render.AWTTextRenderEngine;
import cc.whohow.xet.layout.LayoutEngine;
import com.fasterxml.jackson.databind.JsonNode;
import org.w3c.dom.Document;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Function;

public class ImageXetEngine extends AbstractXetEngine<BufferedImage> {
    private Function<String, Font> fontFactory;
    private Function<String, Color> colorFactory;
    private Function<String, Image> imageFactory;
    private ComponentLayoutEngine<ImageXetContext> layoutEngine;
    private ComponentRenderEngine<ImageXetContext> renderEngine;

    public ImageXetEngine() {
        fontFactory = new CacheableFactory<>(new FontFamilyFactory());
        colorFactory = new CacheableFactory<>(new ColorFactory());
        imageFactory = new CacheableFactory<>(new ImageFactory());

        layoutEngine = new ComponentLayoutEngine<>();
        layoutEngine.addLayoutEngine("img", new AWTImageLayoutEngine<>());
        layoutEngine.addLayoutEngine("p", new AWTChineseTextLayoutEngine<>());

        renderEngine = new ComponentRenderEngine<>();
        renderEngine.addRenderEngine("img", new AWTImageRenderEngine<>());
        renderEngine.addRenderEngine("p", new AWTTextRenderEngine<>());
    }

    @Override
    public BufferedImage process(Document document) {
        VirtualDOM dom = new VirtualDOM();
        dom.setDocument(document);
        JsonNode node = dom.get();

        JsonNode style = node.path("style");
        int width = style.path("width").intValue();
        int height = style.path("height").intValue();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        try (ImageXetContext context = new ImageXetContext(g)) {
            context.setFontFactory(fontFactory);
            context.setColorFactory(colorFactory);
            context.setImageFactory(imageFactory);
            context.setLayoutEngine(layoutEngine);
            context.setRenderEngine(renderEngine);
            context.setDocument(document);
            context.setRenderTree(node);

            context.layout();
            context.render();
            return image;
        }
    }
}
