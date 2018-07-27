package cc.whohow.xet.layout;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 文本排版引擎
 */
public interface TextLayoutEngine<CONTEXT, FONT> extends LayoutEngine<CONTEXT> {
    FONT getFont(CONTEXT context, JsonNode node);

    int getCharacterWidth(FONT font, int codePoint);

    default int[] getCharacterWidths(FONT font, int[] codePoints) {
        return getCharacterWidths(font, codePoints, 0, codePoints.length);
    }

    default int[] getCharacterWidths(FONT font, int[] codePoints, int offset, int length) {
        int[] characterWidths = new int[length];
        for (int  i = 0, j = offset; i < characterWidths.length; i++, j++) {
            characterWidths[i] = getCharacterWidth(font, codePoints[j]);
        }
        return characterWidths;
    }

    int getCharacterHeight(FONT font, int codePoint);

    default int getCharactersHeight(FONT font, int[] codePoints) {
        return getCharactersHeight(font, codePoints, 0, codePoints.length);
    }

    default int getCharactersHeight(FONT font, int[] codePoints, int offset, int length) {
        int lineHeight = 0;
        for (int  i = offset; i < offset + length; i++) {
            lineHeight = Integer.max(lineHeight, getCharacterHeight(font, codePoints[i]));
        }
        return lineHeight;
    }
}
