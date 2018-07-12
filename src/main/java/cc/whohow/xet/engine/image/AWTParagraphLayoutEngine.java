package cc.whohow.xet.engine.image;

import cc.whohow.xet.layout.AbstractParagraphLayoutEngine;

import java.awt.*;

public class AWTParagraphLayoutEngine extends AbstractParagraphLayoutEngine {
    private FontMetrics fontMetrics;

    public FontMetrics getFontMetrics() {
        return fontMetrics;
    }

    public void setFontMetrics(FontMetrics fontMetrics) {
        this.fontMetrics = fontMetrics;
    }

    @Override
    public int getCharacterWidth(int codePoint) {
        return fontMetrics.charWidth(codePoint);
    }

    @Override
    public int getCharacterHeight(int codePoint) {
        return fontMetrics.getHeight();
    }

    @Override
    public int getLineHeight(int[] codePoints) {
        return fontMetrics.getHeight();
    }

    @Override
    public int getLineHeight(int[] codePoints, int offset, int length) {
        return fontMetrics.getHeight();
    }
}
