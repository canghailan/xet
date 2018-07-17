package cc.whohow.xet.layout;

import cc.whohow.xet.box.CharactersBox;
import cc.whohow.xet.box.TextBox;

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
    public void layout(TextBox textBox) {
        textBox.setCharactersBoxes(new ArrayList<>());
        if (textBox.getText() == null || textBox.getText().isEmpty()) {
            return;
        }

        breakLines(textBox);
        adjustLineHeight(textBox);
        adjustLines(textBox);
        adjustTextAlign(textBox);
    }

    /**
     * 断行
     */
    private void breakLines(TextBox textBox) {
        for (String line : LINE.split(textBox.getText())) {
            breakLine(textBox, line);
        }
    }

    /**
     * 断行
     */
    private void breakLine(TextBox textBox, String line) {
        int[] codePoints = line.codePoints().toArray();
        int[] characterWidths = getCharacterWidths(codePoints);

        int start = 0;
        while (start < codePoints.length) {
            int length = 0;
            int lineWidth = 0;
            while (start + length < codePoints.length) {
                int characterWidth = characterWidths[start + length];
                if (lineWidth + characterWidth <= textBox.getWidth()) {
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
            charactersBox.setColor(textBox.getColor());
            charactersBox.setFontFamily(textBox.getFontFamily());
            charactersBox.setFontSize(textBox.getFontSize());
            textBox.getCharactersBoxes().add(charactersBox);

            start += length;
        }
    }

    /**
     * 调整行高
     */
    private void adjustLineHeight(TextBox textBox) {
        if (textBox.getLineHeight() < 0) {
            return;
        }
        for (CharactersBox charactersBox : textBox.getCharactersBoxes()) {
            charactersBox.setHeight(textBox.getLineHeight());
        }
    }

    /**
     * 调整行位置
     */
    private void adjustLines(TextBox textBox) {
        List<CharactersBox> charactersBoxes = textBox.getCharactersBoxes();
        if (charactersBoxes.isEmpty()) {
            return;
        }
        CharactersBox first = charactersBoxes.get(0);
        first.setY(textBox.getY());
        for (int i = 1; i < charactersBoxes.size(); i++) {
            CharactersBox box = charactersBoxes.get(i);
            CharactersBox prev = charactersBoxes.get(i - 1);
            box.setY(prev.getY() + prev.getHeight());
        }
    }

    /**
     * 调整文字对齐方式
     */
    private void adjustTextAlign(TextBox textBox) {
        Objects.requireNonNull(textBox.getTextAlign());
        switch (textBox.getTextAlign()) {
            case "left": {
                for (CharactersBox charactersBox : textBox.getCharactersBoxes()) {
                    charactersBox.setX(textBox.getX());
                }
                break;
            }
            case "right": {
                for (CharactersBox charactersBox : textBox.getCharactersBoxes()) {
                    charactersBox.setX(textBox.getX() + textBox.getWidth() - charactersBox.getWidth());
                }
                break;
            }
            case "center": {
                for (CharactersBox charactersBox : textBox.getCharactersBoxes()) {
                    charactersBox.setX(textBox.getX() + (textBox.getWidth() - charactersBox.getWidth()) / 2);
                }
                break;
            }
            default: {
                throw new IllegalArgumentException(textBox.getTextAlign());
            }
        }
    }
}
