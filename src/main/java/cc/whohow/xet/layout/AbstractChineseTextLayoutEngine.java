package cc.whohow.xet.layout;

import cc.whohow.xet.box.CharactersBox;
import cc.whohow.xet.box.ParagraphBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 排版引擎：
 * 排版核心算法，待优化，计划参考<a href="https://www.w3.org/TR/clreq/">中文排版需求</a>及TEX算法
 */
public abstract class AbstractChineseTextLayoutEngine implements TextLayoutEngine {
    private static final Pattern LINE = Pattern.compile("(\r\n|\r|\n)");

    @Override
    public void layout(ParagraphBox paragraphBox) {
        paragraphBox.setCharactersBoxes(new ArrayList<>());
        if (paragraphBox.getText() == null || paragraphBox.getText().isEmpty()) {
            return;
        }

        breakLines(paragraphBox);
        adjustLineHeight(paragraphBox);
        adjustLines(paragraphBox);
        adjustTextAlign(paragraphBox);
    }

    /**
     * 断行
     */
    private void breakLines(ParagraphBox paragraphBox) {
        for (String line : LINE.split(paragraphBox.getText())) {
            breakLine(paragraphBox, line);
        }
    }

    /**
     * 断行
     */
    private void breakLine(ParagraphBox paragraphBox, String line) {
        int[] codePoints = line.codePoints().toArray();
        int[] characterWidths = getCharacterWidths(codePoints);

        int start = 0;
        while (start < codePoints.length) {
            int length = 0;
            int lineWidth = 0;
            while (start + length < codePoints.length) {
                int characterWidth = characterWidths[start + length];
                if (lineWidth + characterWidth <= paragraphBox.getWidth()) {
                    length++;
                    lineWidth += characterWidth;
                } else {
                    break;
                }
            }
            if (length == 0) {
                length = 1;
                lineWidth = characterWidths[start];
            }

            CharactersBox charactersBox = new CharactersBox();
            charactersBox.setWidth(lineWidth);
            charactersBox.setHeight(getLineHeight(codePoints, start, length));
            if (start == 0 && length == codePoints.length) {
                charactersBox.setText(line);
            } else {
                charactersBox.setText(new String(codePoints, start, length));
            }
            paragraphBox.getCharactersBoxes().add(charactersBox);

            start += length;
        }
    }

    /**
     * 调整行高
     */
    private void adjustLineHeight(ParagraphBox paragraphBox) {
        if (paragraphBox.getLineHeight() < 0) {
            return;
        }
        for (CharactersBox charactersBox : paragraphBox.getCharactersBoxes()) {
            charactersBox.setHeight(paragraphBox.getLineHeight());
        }
    }

    /**
     * 调整行位置
     */
    private void adjustLines(ParagraphBox paragraphBox) {
        List<CharactersBox> charactersBoxes = paragraphBox.getCharactersBoxes();
        if (charactersBoxes.isEmpty()) {
            return;
        }
        CharactersBox first = charactersBoxes.get(0);
        first.setY(paragraphBox.getY());
        for (int i = 1; i < charactersBoxes.size(); i++) {
            CharactersBox box = charactersBoxes.get(i);
            CharactersBox prev = charactersBoxes.get(i - 1);
            box.setY(prev.getY() + prev.getHeight());
        }
    }

    /**
     * 调整文字对齐方式
     */
    private void adjustTextAlign(ParagraphBox paragraphBox) {
        Objects.requireNonNull(paragraphBox.getTextAlign());
        switch (paragraphBox.getTextAlign()) {
            case "left": {
                for (CharactersBox charactersBox : paragraphBox.getCharactersBoxes()) {
                    charactersBox.setX(paragraphBox.getX());
                }
                break;
            }
            case "right": {
                for (CharactersBox charactersBox : paragraphBox.getCharactersBoxes()) {
                    charactersBox.setX(paragraphBox.getX() + paragraphBox.getWidth() - charactersBox.getWidth());
                }
                break;
            }
            case "center": {
                for (CharactersBox charactersBox : paragraphBox.getCharactersBoxes()) {
                    charactersBox.setX(paragraphBox.getX() + (paragraphBox.getWidth() - charactersBox.getWidth()) / 2);
                }
                break;
            }
            default: {
                throw new IllegalArgumentException(paragraphBox.getTextAlign());
            }
        }
    }
}
