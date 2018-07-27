package cc.whohow.xet.engine.awt.layout;

import cc.whohow.xet.engine.awt.AWTXetContext;
import cc.whohow.xet.layout.AbstractChineseTextLayoutEngine;
import cc.whohow.xet.model.FontMeta;

import java.awt.*;
import java.util.function.Function;

public class AWTChineseTextLayoutEngine<CONTEXT extends AWTXetContext> extends AbstractChineseTextLayoutEngine<CONTEXT, FontMetrics> {
    @Override
    public int getCharacterWidth(FontMetrics fontMetrics, int codePoint) {
        return fontMetrics.charWidth(codePoint);
    }

    @Override
    public int getCharacterHeight(FontMetrics fontMetrics, int codePoint) {
        return fontMetrics.getHeight();
    }

    @Override
    public int getCharactersHeight(FontMetrics fontMetrics, int[] codePoints) {
        return fontMetrics.getHeight();
    }

    @Override
    public int getCharactersHeight(FontMetrics fontMetrics, int[] codePoints, int offset, int length) {
        return fontMetrics.getHeight();
    }

    @Override
    protected FontMetrics getFont(CONTEXT context, FontMeta fontMeta) {
        return context.getFontMetrics(fontMeta);
    }
}
