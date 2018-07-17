package cc.whohow.xet.box;

import java.util.List;

/**
 * 文字
 */
public class TextBox extends Box {
    private String text;
    private String color;
    private String fontFamily;
    private int fontSize;
    private String textAlign;
    private int lineHeight;
    private List<CharactersBox> charactersBoxes;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(String textAlign) {
        this.textAlign = textAlign;
    }

    public int getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
    }

    public List<CharactersBox> getCharactersBoxes() {
        return charactersBoxes;
    }

    public void setCharactersBoxes(List<CharactersBox> charactersBoxes) {
        this.charactersBoxes = charactersBoxes;
    }
}
