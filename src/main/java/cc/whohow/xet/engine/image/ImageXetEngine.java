package cc.whohow.xet.engine.image;

import cc.whohow.xet.AbstractXetEngine;
import cc.whohow.xet.context.CacheableFactory;
import cc.whohow.xet.context.ColorFactory;
import cc.whohow.xet.context.FontFactory;
import cc.whohow.xet.context.ImageFactory;
import cc.whohow.xet.engine.image.layout.AWTChineseTextLayoutEngine;
import cc.whohow.xet.engine.image.layout.AWTImageLayoutEngine;
import cc.whohow.xet.engine.image.render.DefaultRender;
import cc.whohow.xet.engine.image.render.ImageRender;
import cc.whohow.xet.engine.image.render.ParagraphRender;
import cc.whohow.xet.layout.DefaultLayoutEngine;
import cc.whohow.xet.layout.LayoutEngine;
import com.fasterxml.jackson.databind.JsonNode;
import org.w3c.dom.Document;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ImageXetEngine extends AbstractXetEngine<BufferedImage> {
    private Function<String, Font> fontFactory;
    private Function<String, Color> colorFactory;
    private Function<String, Image> imageFactory;
    private LayoutEngine layoutEngine;
    private BiConsumer<ImageXetContext, JsonNode> render;

    public ImageXetEngine() {
        this.fontFactory = new CacheableFactory<>(new FontFactory());
        this.colorFactory = new CacheableFactory<>(new ColorFactory());
        this.imageFactory = new CacheableFactory<>(new ImageFactory());

        DefaultLayoutEngine layoutEngine = new DefaultLayoutEngine(layoutEngineFactory);
        layoutEngine.addLayoutEngine("img", new AWTImageLayoutEngine(imageFactory));
        layoutEngine.addLayoutEngine("p", new AWTChineseTextLayoutEngine());
        this.layoutEngine = layoutEngine;

        DefaultRender render = new DefaultRender();
        render.addRender("img", new ImageRender());
        render.addRender("p", new ParagraphRender());
        this.render = render;
    }

    public Function<JsonNode, LayoutEngine> createLayoutEngine(JsonNode node) {
        
    }

    @Override
    public BufferedImage process(Document document) {
        ImageXetContext context = new ImageXetContext(document);
        context.setFontFactory(fontFactory);
        context.setColorFactory(colorFactory);
        context.setImageFactory(imageFactory);

        JsonNode node = context.getVirtualDOM();

        layoutEngine.layout(node);
        System.out.println(node);

        JsonNode computedStyle = node.path("computedStyle");
        int width = computedStyle.path("width").intValue();
        int height = computedStyle.path("height").intValue();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        context.setGraphics(g);
        render.accept(context, node);
        g.dispose();

        return image;
    }
}
