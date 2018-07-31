package cc.whohow.xet.engine.awt.model;

import cc.whohow.xet.model.FontMeta;

import java.awt.*;
import java.util.function.Function;

public class FontMetricsFactory implements Function<FontMeta, FontMetrics> {
    protected final Function<FontMeta, Font> fontFactory;
    protected final Graphics graphics;

    public FontMetricsFactory(Function<FontMeta, Font> fontFactory, Graphics graphics) {
        this.fontFactory = fontFactory;
        this.graphics = graphics;
    }

    @Override
    public FontMetrics apply(FontMeta fontMeta) {
        return graphics.getFontMetrics(fontFactory.apply(fontMeta));
    }
}
