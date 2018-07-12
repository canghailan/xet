package cc.whohow.xet.context;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

public class BaseXetContext implements XetContext {
    protected static final Pattern SPACES = Pattern.compile("\\s+");
    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(new YAMLFactory());

    protected final Document document;
    protected final Map<String, ObjectNode> styles;
    protected final Map<Element, ObjectNode> computedStyles = new IdentityHashMap<>();

    public BaseXetContext(Document document) {
        this.document = document;
        this.styles = parseStyles();
    }

    @Override
    public Document getDocument() {
        return document;
    }

    protected Map<String, ObjectNode> parseStyles() {
        Map<String, ObjectNode> styles = new HashMap<>();
        NodeList styleList = document.getElementsByTagName("style");
        for (int i = 0; i < styleList.getLength(); i++) {
            Element style = (Element) styleList.item(i);
            Iterator<Map.Entry<String, JsonNode>> iterator = parse(style.getTextContent()).fields();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> e = iterator.next();
                styles.put(e.getKey(), (ObjectNode) e.getValue());
            }
        }
        return styles;
    }

    @Override
    public ObjectNode getStyle(Element element) {
        String style = element.getAttribute("style");
        if (style.isEmpty()) {
            return null;
        }
        return SPACES.splitAsStream(style)
                .map(styles::get)
                .reduce(OBJECT_MAPPER.createObjectNode(), this::merge);
    }

    protected ObjectNode computedStyle(Element element) {
        ObjectNode style = getStyle(element);
        if (style == null) {
            return empty();
        }
        return style;
    }

    @Override
    public ObjectNode getComputedStyle(Element element) {
        return computedStyles.computeIfAbsent(element, this::computedStyle);
    }

    public int getX(ObjectNode style) {
        return style.path("x").asInt(-1);
    }

    public int getY(ObjectNode style) {
        return style.path("y").asInt(-1);
    }

    public int getWidth(ObjectNode style) {
        return style.path("width").asInt(-1);
    }

    public int getHeight(ObjectNode style) {
        return style.path("height").asInt(-1);
    }

    private ObjectNode empty() {
        return OBJECT_MAPPER.createObjectNode();
    }

    private ObjectNode parse(String object) {
        try {
            return OBJECT_MAPPER.readValue(object, ObjectNode.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private ObjectNode merge(ObjectNode a, ObjectNode b) {
        a.setAll(b);
        return a;
    }
}
