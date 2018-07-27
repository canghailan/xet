package cc.whohow.xet.engine.image;

import cc.whohow.xet.context.XetContext;
import cc.whohow.xet.dom.XetVirtualDOM;
import com.fasterxml.jackson.databind.JsonNode;
import org.w3c.dom.Document;

import java.awt.*;
import java.util.function.Function;

public class ImageXetContext implements XetContext {
    protected Document document;
    protected Function<String, Font> fontFactory;
    protected Function<String, Color> colorFactory;
    protected Function<String, Image> imageFactory;
    protected Graphics graphics;

    public ImageXetContext(Document document) {
        this.document = document;
    }

    public void setFontFactory(Function<String, Font> fontFactory) {
        this.fontFactory = fontFactory;
    }

    public void setColorFactory(Function<String, Color> colorFactory) {
        this.colorFactory = colorFactory;
    }

    public void setImageFactory(Function<String, Image> imageFactory) {
        this.imageFactory = imageFactory;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public void setGraphics(Graphics graphics) {
        this.graphics = graphics;
    }

    public Font getFont(String fontFamily) {
        return fontFactory.apply(fontFamily);
    }

    public Font getFont(String fontFamily, int fontSize) {
        return getFont(fontFamily).deriveFont((float) fontSize);
    }

    public Color getColor(String colorCode) {
        return colorFactory.apply(colorCode);
    }

    public Image getImage(String url) {
        return imageFactory.apply(url);
    }

    @Override
    public Document getDocument() {
        return document;
    }

    @Override
    public JsonNode getVirtualDOM() {
        return new XetVirtualDOM(document).get();
    }
}
