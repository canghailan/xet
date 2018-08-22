package cc.whohow.xet.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Objects;

public class FontMeta {
    private String fontFamily;
    private int fontSize;
    private String fontStyle;
    private String fontWeight;

    public String getFontFamily() {
        return fontFamily;
    }

    public int getFontSize() {
        return fontSize;
    }

    public String getFontStyle() {
        return fontStyle;
    }

    public String getFontWeight() {
        return fontWeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FontMeta fontMeta = (FontMeta) o;
        return fontSize == fontMeta.fontSize &&
                Objects.equals(fontFamily, fontMeta.fontFamily) &&
                Objects.equals(fontStyle, fontMeta.fontStyle) &&
                Objects.equals(fontWeight, fontMeta.fontWeight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fontFamily, fontSize, fontStyle, fontWeight);
    }

    public static class Builder {
        private FontMeta fontMeta = new FontMeta();

        public Builder withFontFamily(String fontFamily) {
            fontMeta.fontFamily = fontFamily;
            return this;
        }

        public Builder withFontSize(int fontSize) {
            fontMeta.fontSize = fontSize;
            return this;
        }

        public Builder withFontStyle(String fontStyle) {
            fontMeta.fontStyle = fontStyle;
            return this;
        }

        public Builder withFontWeight(String fontWeight) {
            fontMeta.fontWeight = fontWeight;
            return this;
        }

        public Builder withStyle(JsonNode style) {
            fontMeta.fontFamily = style.path("font-family").textValue();
            fontMeta.fontSize = style.path("font-size").intValue();
            fontMeta.fontStyle = style.path("font-style").textValue();
            fontMeta.fontWeight = style.path("font-weight").textValue();
            return this;
        }

        public FontMeta get() {
            return fontMeta;
        }
    }
}
