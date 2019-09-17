package cc.whohow.xet.engine.image;

import cc.whohow.xet.AbstractXetEngine;
import cc.whohow.xet.engine.awt.layout.AWTChineseTextLayoutEngine;
import cc.whohow.xet.engine.awt.layout.AWTImageLayoutEngine;
import cc.whohow.xet.engine.awt.model.ColorFactory;
import cc.whohow.xet.engine.awt.model.FontFamilyFactory;
import cc.whohow.xet.engine.awt.model.ImageFactory;
import cc.whohow.xet.engine.awt.render.*;
import cc.whohow.xet.layout.ComponentLayoutEngine;
import cc.whohow.xet.layout.NoopLayoutEngine;
import cc.whohow.xet.model.RenderTreeBuilder;
import cc.whohow.xet.model.Styles;
import cc.whohow.xet.render.ComponentRenderEngine;
import cc.whohow.xet.util.CacheableFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
        layoutEngine.addLayoutEngine("rect", new NoopLayoutEngine<>());
        layoutEngine.addLayoutEngine("circle", new NoopLayoutEngine<>());
        layoutEngine.addLayoutEngine("ellipse", new NoopLayoutEngine<>());
        layoutEngine.addLayoutEngine("line", new NoopLayoutEngine<>());
        layoutEngine.addLayoutEngine("polygon", new NoopLayoutEngine<>());
        layoutEngine.addLayoutEngine("polyline", new NoopLayoutEngine<>());

        renderEngine = new ComponentRenderEngine<>();
        renderEngine.addRenderEngine("img", new AWTImageRenderEngine<>());
        renderEngine.addRenderEngine("p", new AWTTextRenderEngine<>());
        renderEngine.addRenderEngine("rect", new AWTRectRenderEngine<>());
        renderEngine.addRenderEngine("circle", new AWTCircleRenderEngine<>());
        renderEngine.addRenderEngine("ellipse", new AWTEllipseRenderEngine<>());
        renderEngine.addRenderEngine("line", new AWTLineRenderEngine<>());
        renderEngine.addRenderEngine("polygon", new AWTPolygonRenderEngine<>());
        renderEngine.addRenderEngine("polyline", new AWTPolylineRenderEngine<>());
    }

    @Override
    public BufferedImage process(Document document) {
        RenderTreeBuilder renderTreeBuilder = new RenderTreeBuilder();
        renderTreeBuilder.setDocument(document);
        JsonNode renderTree = renderTreeBuilder.get();

        ObjectNode style = Styles.getStyle(renderTree);
        int width = Styles.WIDTH.getInt(style);
        int height = Styles.HEIGHT.getInt(style);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.setColor(Color.BLACK);

        try (ImageXetContext context = new ImageXetContext(g)) {
            context.setFontFactory(fontFactory);
            context.setColorFactory(colorFactory);
            context.setImageFactory(imageFactory);
            context.setLayoutEngine(layoutEngine);
            context.setRenderEngine(renderEngine);
            context.setDocument(document);
            context.setRenderTree(renderTree);

            context.layout();
            context.render();
            return image;
        }
    }
}
