package cc.whohow.xet.layout;

import cc.whohow.xet.model.FontMeta;
import cc.whohow.xet.model.Styles;
import cc.whohow.xet.util.Json;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.regex.Pattern;

/**
 * 排版引擎：
 * 排版核心算法，待优化，计划参考<a href="https://www.w3.org/TR/clreq/">中文排版需求</a>及TEX算法
 */
public abstract class AbstractChineseTextLayoutEngine<CONTEXT, FONT> implements TextLayoutEngine<CONTEXT, FONT> {
    private static final Pattern LINE = Pattern.compile("(\r\n|\r|\n)");

    @Override
    public void layout(CONTEXT context, JsonNode node) {
        ObjectNode textNode = (ObjectNode) node;
        String text = node.path("text").textValue();
        if (text == null || text.isEmpty()) {
            return;
        }

        textNode.putArray("textLayout");
        breakLines(textNode, getFont(context, textNode));
        adjustLineHeight(textNode);
        adjustLines(textNode);
        adjustTextAlign(textNode);

        updateLayout(textNode);
    }

    /**
     * 断行
     */
    protected void breakLines(JsonNode textNode, FONT font) {
        String text = textNode.path("text").textValue();
        for (String line : LINE.split(text)) {
            breakLine(textNode, line, font);
        }
    }

    /**
     * 断行
     */
    protected void breakLine(JsonNode textNode, String line, FONT font) {
        ArrayNode textLayout = getTextLayout(textNode);

        ObjectNode style = Styles.getComputedStyle(textNode);
        int width = Styles.WIDTH.getInt(style);
        JsonNode color = Styles.COLOR.getValue(style);
        JsonNode fontFamily = Styles.FONT_FAMILY.getValue(style);
        JsonNode fontSize = Styles.FONT_SIZE.getValue(style);

        int[] codePoints = line.codePoints().toArray();
        int[] characterWidths = getCharacterWidths(font, codePoints);

        int start = 0;
        while (start < codePoints.length) {
            int length = 0;
            int lineWidth = 0;
            while (start + length < codePoints.length) {
                int characterWidth = characterWidths[start + length];
                if (lineWidth + characterWidth <= width) {
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
            int height = getCharactersHeight(font, codePoints, start, length);

            ObjectNode node = Json.newObject();
            node.put("tagName", "text");
            if (start == 0 && length == codePoints.length) {
                node.put("text", line);
            } else {
                node.put("text", new String(codePoints, start, length));
            }
            ObjectNode computedStyle = Json.newObject();
            Styles.WIDTH.setInt(computedStyle, lineWidth);
            Styles.HEIGHT.setInt(computedStyle, height);
            Styles.LINE_HEIGHT.setInt(computedStyle, height);
            Styles.COLOR.setValue(computedStyle, color);
            Styles.FONT_FAMILY.setValue(computedStyle, fontFamily);
            Styles.FONT_SIZE.setValue(computedStyle, fontSize);
            node.set("computedStyle", computedStyle);
            textLayout.add(node);

            start += length;
        }
    }

    /**
     * 调整行高
     */
    protected void adjustLineHeight(JsonNode textNode) {
        ObjectNode style = Styles.getComputedStyle(textNode);
        if (Styles.LINE_HEIGHT.isNull(style)) {
            return;
        }
        JsonNode lineHeight = Styles.LINE_HEIGHT.getValue(style);
        for (JsonNode line : getTextLayout(textNode)) {
            ObjectNode lineStyle = Styles.getComputedStyle(line);
            Styles.HEIGHT.setValue(lineStyle, lineHeight);
            Styles.LINE_HEIGHT.setValue(lineStyle, lineHeight);
        }
    }

    /**
     * 调整行位置
     */
    protected void adjustLines(JsonNode textNode) {
        ArrayNode textLayout = getTextLayout(textNode);
        if (textLayout.size() == 0) {
            return;
        }

        ObjectNode computedStyle = Styles.getComputedStyle(textNode);
        int y = Styles.Y.getInt(computedStyle);
        for (JsonNode line : getTextLayout(textNode)) {
            ObjectNode lineStyle = Styles.getComputedStyle(line);

            Styles.Y.setInt(lineStyle, y);

            y += Styles.LINE_HEIGHT.getInt(lineStyle);
        }
    }

    /**
     * 调整文字对齐方式
     */
    protected void adjustTextAlign(JsonNode textNode) {
        ObjectNode style = Styles.getComputedStyle(textNode);

        if (Styles.TEXT_ALIGN.isNull(style)) {
            throw new IllegalArgumentException("text-align: null");
        }

        String textAlign = Styles.TEXT_ALIGN.get(style);
        switch (textAlign) {
            case "left": {
                JsonNode x = Styles.X.getValue(style);
                for (JsonNode line : getTextLayout(textNode)) {
                    Styles.X.setValue(Styles.getComputedStyle(line), x);
                }
                break;
            }
            case "right": {
                int x = Styles.X.getInt(style);
                int width = Styles.WIDTH.getInt(style);
                for (JsonNode line : getTextLayout(textNode)) {
                    ObjectNode lineStyle = Styles.getComputedStyle(line);
                    Styles.X.setInt(lineStyle, x + width - Styles.WIDTH.getInt(lineStyle));
                }
                break;
            }
            case "center": {
                int x = Styles.X.getInt(style);
                int width = Styles.WIDTH.getInt(style);
                for (JsonNode line : getTextLayout(textNode)) {
                    ObjectNode lineStyle = Styles.getComputedStyle(line);
                    Styles.X.setInt(lineStyle, x + (width - Styles.WIDTH.getInt(lineStyle)) / 2);
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("text-align: " + textAlign);
            }
        }
    }

    protected int calculateHeight(ObjectNode textNode) {
        int height = 0;
        for (JsonNode line : getTextLayout(textNode)) {
            height += Styles.LINE_HEIGHT.getInt(Styles.getComputedStyle(line));
        }
        return height;
    }

    protected void updateLayout(ObjectNode textNode) {
        ObjectNode style = Styles.getComputedStyle(textNode);
        if (Styles.HEIGHT.isNull(style)) {
            Styles.HEIGHT.setInt(style, calculateHeight(textNode));
        }
    }

    protected ArrayNode getTextLayout(JsonNode node) {
        return (ArrayNode) node.path("textLayout");
    }

    @Override
    public FONT getFont(CONTEXT context, JsonNode node) {
        FontMeta fontMeta = new FontMeta.Builder()
                .withStyle(Styles.getComputedStyle(node))
                .get();
        return getFont(context, fontMeta);
    }

    protected abstract FONT getFont(CONTEXT context, FontMeta fontMeta);
}
