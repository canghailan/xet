package cc.whohow.xet.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Style {
    protected final String name;

    public Style(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public JsonNode getValue(ObjectNode style) {
        return style.path(name);
    }

    public void setValue(ObjectNode style, JsonNode value) {
        style.set(name, value);
    }

    public String get(ObjectNode style) {
        return getValue(style).asText();
    }

    public String get(ObjectNode style, String defaultValue) {
        return getValue(style).asText(defaultValue);
    }

    public void set(ObjectNode style, String value) {
        style.put(name, value);
    }

    public boolean isNull(ObjectNode style) {
        JsonNode value = getValue(style);
        return value.isMissingNode() || value.isNull();
    }

    public static class IntValue extends Style {
        public IntValue(String name) {
            super(name);
        }

        public int getInt(ObjectNode style) {
            return getValue(style).asInt();
        }

        public int getInt(ObjectNode style, int defaultValue) {
            return getValue(style).asInt(defaultValue);
        }

        public void setInt(ObjectNode style, int value) {
            style.put(name, value);
        }
    }
}
