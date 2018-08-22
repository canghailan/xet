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
        int style = Font.PLAIN;
        if ("bold".equals(fontMeta.getFontWeight())) {
            style |= Font.BOLD;
        }
        if ("italic".equals(fontMeta.getFontStyle())) {
            style |= Font.ITALIC;
        }
        return fontFamilyFactory.apply(fontMeta.getFontFamily())
                .deriveFont(style, fontMeta.getFontSize());
    }
}
