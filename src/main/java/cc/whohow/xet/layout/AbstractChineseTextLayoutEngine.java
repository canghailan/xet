package cc.whohow.xet.layout;

import cc.whohow.xet.model.FontMeta;
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
        String text = textNode.path("text").textValue();
        if (text == null || text.isEmpty()) {
            return;
        }

        textNode.putArray("textLayout");
        breakLines(textNode, getFont(context, textNode));
        adjustLineHeight(textNode);
        adjustLines(textNode);
        adjustTextAlign(textNode);
    }

    /**
     * 断行
     */
    private void breakLines(JsonNode textNode, FONT font) {
        String text = textNode.path("text").textValue();
        for (String line : LINE.split(text)) {
            breakLine(textNode, line, font);
        }
    }

    /**
     * 断行
     */
    private void breakLine(JsonNode textNode, String line, FONT font) {
        ArrayNode textLayout = getTextLayout(textNode);

        JsonNode style = getComputedStyle(textNode);
        int width = style.path("width").intValue();
        JsonNode color = style.path("color");
        JsonNode fontFamily = style.path("font-family");
        JsonNode fontSize = style.path("font-size");

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
            computedStyle.put("width", lineWidth);
            computedStyle.put("height", height);
            computedStyle.put("line-height", height);
            computedStyle.set("color", color);
            computedStyle.set("font-family", fontFamily);
            computedStyle.set("font-size", fontSize);
            node.set("computedStyle", computedStyle);
            textLayout.add(node);

            start += length;
        }
    }

    /**
     * 调整行高
     */
    private void adjustLineHeight(JsonNode textNode) {
        JsonNode style = getComputedStyle(textNode);

        JsonNode lineHeight = style.path("line-height");
        if (lineHeight.isMissingNode() || lineHeight.isNull()) {
            return;
        }
        for (JsonNode node : getTextLayout(textNode)) {
            getComputedStyle(node).set("line-height", lineHeight);
        }
    }

    /**
     * 调整行位置
     */
    private void adjustLines(JsonNode textNode) {
        ArrayNode textLayout = getTextLayout(textNode);
        if (textLayout.size() == 0) {
            return;
        }

        ObjectNode computedStyle = getComputedStyle(textNode);
        int y = computedStyle.path("y").intValue();
        JsonNode first = textLayout.get(0);
        getComputedStyle(first).put("y", y);
        for (int i = 1; i < textLayout.size(); i++) {
            y += getComputedStyle(textLayout.get(i - 1))
                    .path("line-height").intValue();

            getComputedStyle(textLayout.get(i))
                    .put("y", y);
        }
    }

    /**
     * 调整文字对齐方式
     */
    private void adjustTextAlign(JsonNode textNode) {
        ObjectNode style = getComputedStyle(textNode);

        JsonNode textAlign = style.path("text-align");
        if (textAlign.isMissingNode() || textAlign.isNull()) {
            throw new IllegalArgumentException("text-align: null");
        }

        switch (textAlign.textValue()) {
            case "left": {
                JsonNode x = style.path("x");
                for (JsonNode line : getTextLayout(textNode)) {
                    getComputedStyle(line).set("x", x);
                }
                break;
            }
            case "right": {
                int x = style.path("x").intValue();
                int width = style.path("width").intValue();
                for (JsonNode line : getTextLayout(textNode)) {
                    ObjectNode lineStyle = getComputedStyle(line);
                    lineStyle.put("x", x + width - lineStyle.path("width").intValue());
                }
                break;
            }
            case "center": {
                int x = style.path("x").intValue();
                int width = style.path("width").intValue();
                for (JsonNode line : getTextLayout(textNode)) {
                    ObjectNode lineStyle = getComputedStyle(line);
                    lineStyle.put("x", x + (width - lineStyle.path("width").intValue()) / 2);
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("text-align: " + textAlign.textValue());
            }
        }
    }

    protected ArrayNode getTextLayout(JsonNode node) {
        return (ArrayNode) node.path("textLayout");
    }

    protected ObjectNode getComputedStyle(JsonNode node) {
        return (ObjectNode) node.path("computedStyle");
    }

    @Override
    public FONT getFont(CONTEXT context, JsonNode node) {
        FontMeta fontMeta = new FontMeta.Builder()
                .withStyle(getComputedStyle(node))
                .get();
        return getFont(context, fontMeta);
    }

    protected abstract FONT getFont(CONTEXT context, FontMeta fontMeta);
}
