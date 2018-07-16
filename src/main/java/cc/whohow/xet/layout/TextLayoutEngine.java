package cc.whohow.xet.layout;

import cc.whohow.xet.box.ParagraphBox;

import java.util.Arrays;

/**
 * 段落排版引擎
 */
public interface TextLayoutEngine {
    void layout(ParagraphBox paragraph);

    int getCharacterWidth(int codePoint);

    default int[] getCharacterWidths(int[] codePoints) {
        return getCharacterWidths(codePoints, 0, codePoints.length);
    }

    default int[] getCharacterWidths(int[] codePoints, int offset, int length) {
        return Arrays.stream(codePoints, offset, length - offset)
                .map(this::getCharacterWidth)
                .toArray();
    }

    int getCharacterHeight(int codePoint);

    default int getLineHeight(int[] codePoints) {
        return getLineHeight(codePoints, 0, codePoints.length);
    }

    default int getLineHeight(int[] codePoints, int offset, int length) {
        return Arrays.stream(codePoints, offset, length - offset)
                .map(this::getCharacterHeight)
                .max()
                .orElse(-1);
    }
}
