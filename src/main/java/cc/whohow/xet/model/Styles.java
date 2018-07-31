package cc.whohow.xet.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Styles {
    public static Style.IntValue X = new Style.IntValue("x");
    public static Style.IntValue Y = new Style.IntValue("y");
    public static Style.IntValue WIDTH = new Style.IntValue("width");
    public static Style.IntValue HEIGHT = new Style.IntValue("height");
    public static Style.IntValue MARGIN_TOP = new Style.IntValue("margin-top");
    public static Style.IntValue MARGIN_RIGHT = new Style.IntValue("margin-right");
    public static Style.IntValue MARGIN_BOTTOM = new Style.IntValue("margin-bottom");
    public static Style.IntValue MARGIN_LEFT = new Style.IntValue("margin-left");
    public static Style.IntValue BORDER_TOP = new Style.IntValue("border-top");
    public static Style.IntValue BORDER_RIGHT = new Style.IntValue("border-right");
    public static Style.IntValue BORDER_BOTTOM = new Style.IntValue("border-bottom");
    public static Style.IntValue BORDER_LEFT = new Style.IntValue("border-left");
    public static Style.IntValue PADDING_TOP = new Style.IntValue("padding-top");
    public static Style.IntValue PADDING_RIGHT = new Style.IntValue("padding-right");
    public static Style.IntValue PADDING_BOTTOM = new Style.IntValue("padding-bottom");
    public static Style.IntValue PADDING_LEFT = new Style.IntValue("padding-left");
    public static Style COLOR = new Style("color");
    public static Style FONT_FAMILY = new Style("font-family");
    public static Style.IntValue FONT_SIZE = new Style.IntValue("font-size");
    public static Style.IntValue LINE_HEIGHT = new Style.IntValue("line-height");
    public static Style TEXT_ALIGN = new Style("text-align");

    public static ObjectNode getStyle(JsonNode node) {
        return (ObjectNode) node.path("style");
    }

    public static ObjectNode getComputedStyle(JsonNode node) {
        return (ObjectNode) node.path("computedStyle");
    }
}
