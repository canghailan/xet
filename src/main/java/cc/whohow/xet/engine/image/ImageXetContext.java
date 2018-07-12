package cc.whohow.xet.engine.image;

import cc.whohow.xet.context.BaseXetContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ImageXetContext extends BaseXetContext {
    protected Function<String, Font> fontFactory;
    protected Function<String, Color> colorFactory;
    protected Function<String, Image> imageFactory;
    protected Map<String, BiConsumer<ImageXetContext, Element>> elementHandlers;
    protected Graphics graphics;

    public ImageXetContext(Document document) {
        super(document);
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

    public void setElementHandlers(Map<String, BiConsumer<ImageXetContext, Element>> elementHandlers) {
        this.elementHandlers = elementHandlers;
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

    public BiConsumer<ImageXetContext, Element> getElementHandler(Element element) {
        String tagName = element.getTagName();
        BiConsumer<ImageXetContext, Element> handler = elementHandlers.get(tagName);
        if (handler != null) {
            return handler;
        }
        return elementHandlers.get("*");
    }

    public void accept(Element element) {
        getElementHandler(element).accept(this, element);
    }

    public void accept(Document document) {
        accept(document.getDocumentElement());
    }
}
