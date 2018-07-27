package cc.whohow.xet.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.UncheckedIOException;

public class Json {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(new YAMLFactory());

    public static ArrayNode newArray() {
        return OBJECT_MAPPER.createArrayNode();
    }

    public static ObjectNode newObject() {
        return OBJECT_MAPPER.createObjectNode();
    }

    public static JsonNode parse(String json) {
        try {
            return OBJECT_MAPPER.readTree(json);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T parse(String json, Class<T> type) {
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String stringify(Object json) {
        try {
            return OBJECT_MAPPER.writeValueAsString(json);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static ObjectNode assign(ObjectNode a, ObjectNode b) {
        a.setAll(b);
        return a;
    }
}
