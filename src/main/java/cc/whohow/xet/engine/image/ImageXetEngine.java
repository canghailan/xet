package cc.whohow.xet.engine.image;

import cc.whohow.xet.AbstractXetEngine;
import cc.whohow.xet.context.CacheableFactory;
import cc.whohow.xet.context.ColorFactory;
import cc.whohow.xet.context.FontFactory;
import cc.whohow.xet.context.ImageFactory;
import cc.whohow.xet.engine.image.handler.DelegateHandler;
import cc.whohow.xet.engine.image.handler.ImageHandler;
import cc.whohow.xet.engine.image.handler.NoneHandler;
import cc.whohow.xet.engine.image.handler.ParagraphHandler;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ImageXetEngine extends AbstractXetEngine<BufferedImage> {
    private Function<String, Font> fontFactory = new CacheableFactory<>(new FontFactory());
    private Function<String, Color> colorFactory = new CacheableFactory<>(new ColorFactory());
    private Function<String, Image> imageFactory = new CacheableFactory<>(new ImageFactory());
    private Map<String, BiConsumer<ImageXetContext, Element>> elementHandlers = new HashMap<>(defaultElementHandler());

    private static Map<String, BiConsumer<ImageXetContext, Element>> defaultElementHandler() {
        Map<String, BiConsumer<ImageXetContext, Element>> renders = new HashMap<>();
        renders.put("*", new DelegateHandler());
        renders.put("style", new NoneHandler());
        renders.put("img", new ImageHandler());
        renders.put("p", new ParagraphHandler());
        return renders;
    }

    @Override
    public BufferedImage process(Document document) {
        ImageXetContext context = new ImageXetContext(document);
        context.setFontFactory(fontFactory);
        context.setColorFactory(colorFactory);
        context.setImageFactory(imageFactory);
        context.setElementHandlers(elementHandlers);

        ObjectNode style = context.getComputedStyle(document.getDocumentElement());
        int width = context.getWidth(style);
        int height = context.getHeight(style);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        context.setGraphics(g);
        context.accept(document);
        g.dispose();
        return image;
    }

    public Function<String, Font> getFontFactory() {
        return fontFactory;
    }

    public void setFontFactory(Function<String, Font> fontFactory) {
        this.fontFactory = fontFactory;
    }

    public Function<String, Color> getColorFactory() {
        return colorFactory;
    }

    public void setColorFactory(Function<String, Color> colorFactory) {
        this.colorFactory = colorFactory;
    }

    public Function<String, Image> getImageFactory() {
        return imageFactory;
    }

    public void setImageFactory(Function<String, Image> imageFactory) {
        this.imageFactory = imageFactory;
    }

    public Map<String, BiConsumer<ImageXetContext, Element>> getElementHandlers() {
        return elementHandlers;
    }

    public void setElementHandlers(Map<String, BiConsumer<ImageXetContext, Element>> elementHandlers) {
        this.elementHandlers = elementHandlers;
    }
}
