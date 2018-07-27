package cc.whohow.xet.engine.awt;

import cc.whohow.xet.XetContext;
import cc.whohow.xet.engine.awt.model.FontFactory;
import cc.whohow.xet.engine.awt.model.FontMetricsFactory;
import cc.whohow.xet.model.FontMeta;
import cc.whohow.xet.util.CacheableFactory;
import com.fasterxml.jackson.databind.JsonNode;
import org.w3c.dom.Document;

import java.awt.*;
import java.io.Closeable;
import java.util.function.Function;

public abstract class AWTXetContext implements XetContext, Closeable {
    protected final Graphics graphics;
    protected Document document;
    protected JsonNode renderTree;
    protected Function<String, Color> colorFactory;
    protected Function<String, Image> imageFactory;
    protected Function<FontMeta, Font> fontFactory;
    protected Function<FontMeta, FontMetrics> fontMetricsFactory;

    public AWTXetContext(Graphics graphics) {
        this.graphics = graphics;
    }

    @Override
    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    @Override
    public JsonNode getRenderTree() {
        return renderTree;
    }

    public void setRenderTree(JsonNode renderTree) {
        this.renderTree = renderTree;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public void setFontFactory(Function<String, Font> fontFactory) {
        this.fontFactory = new CacheableFactory<>(new FontFactory(fontFactory));
        this.fontMetricsFactory = new CacheableFactory<>(new FontMetricsFactory(this.fontFactory, graphics));
    }

    public void setColorFactory(Function<String, Color> colorFactory) {
        this.colorFactory = colorFactory;
    }

    public void setImageFactory(Function<String, Image> imageFactory) {
        this.imageFactory = imageFactory;
    }

    public Font getFont(FontMeta fontMeta) {
        return fontFactory.apply(fontMeta);
    }

    public FontMetrics getFontMetrics(FontMeta fontMeta) {
        return fontMetricsFactory.apply(fontMeta);
    }

    public Color getColor(String colorCode) {
        return colorFactory.apply(colorCode);
    }

    public Image getImage(String url) {
        return imageFactory.apply(url);
    }

    @Override
    public void close() {
        graphics.dispose();
    }
}
