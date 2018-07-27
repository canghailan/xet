package cc.whohow.xet.engine.awt.model;

import cc.whohow.xet.model.FontMeta;

import java.awt.*;
import java.util.function.Function;

public class FontFactory implements Function<FontMeta, Font> {
    private final Function<String, Font> fontFamilyFactory;

    public FontFactory(Function<String, Font> fontFamilyFactory) {
        this.fontFamilyFactory = fontFamilyFactory;
    }

    @Override
    public Font apply(FontMeta fontMeta) {
        return fontFamilyFactory.apply(fontMeta.getFontFamily())
                .deriveFont((float) fontMeta.getFontSize());
    }
}
