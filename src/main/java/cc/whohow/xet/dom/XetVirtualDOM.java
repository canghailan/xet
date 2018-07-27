package cc.whohow.xet.dom;

import cc.whohow.xet.json.Json;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.w3c.dom.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class XetVirtualDOM implements Supplier<JsonNode> {
    protected static final Pattern SPACES = Pattern.compile("\\s+");

    protected final Document document;
    protected JsonNode virtualDOM;
    protected Map<String, ObjectNode> styles;

    public XetVirtualDOM(Document document) {
        this.document = document;
    }

    public JsonNode visit(Element element) {
        if ("style".equals(element.getTagName())) {
            return null;
        }

        ObjectNode node = Json.newObject();
        node.put("tagName", element.getTagName());
        setProps(element, node);
        setChildren(element, node);
        setStyle(element, node);
        return node;
    }

    protected void setProps(Element element, ObjectNode node) {
        NamedNodeMap attributes = element.getAttributes();
        if (attributes == null || attributes.getLength() == 0) {
            return;
        }
        ObjectNode props = Json.newObject();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            props.put(attribute.getNodeName(), attribute.getNodeValue());
        }
        node.set("props", props);
    }

    protected void setChildren(Element element, ObjectNode node) {
        NodeList childNodes = element.getChildNodes();
        if (childNodes == null || childNodes.getLength() == 0) {
            return;
        }
        ArrayNode children = Json.newArray();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child instanceof Element) {
                JsonNode childNode = visit((Element) child);
                if (childNode != null) {
                    children.add(childNode);
                }
            }
        }
        if (children.size() > 0) {
            node.set("children", children);
        } else {
            node.put("text", element.getTextContent());
        }
    }

    protected void setStyle(Element element, ObjectNode node) {
        String styleExpr = node.path("props").path("style").textValue();
        if (styleExpr == null || styleExpr.isEmpty()) {
            node.set("computedStyle", Json.newObject());
            return;
        }
        ObjectNode style =  SPACES.splitAsStream(styleExpr)
                .map(styles::get)
                .reduce(Json.newObject(), Json::assign);
        if (style.size() == 0) {
            node.set("computedStyle", style);
        } else {
            node.set("style", style);
            node.set("computedStyle", style.deepCopy());
        }
    }

    @Override
    public JsonNode get() {
        if (virtualDOM == null) {
            styles = parseStyles();
            virtualDOM = visit(document.getDocumentElement());
        }
        System.out.println(virtualDOM);
        return virtualDOM;
    }

    protected Map<String, ObjectNode> parseStyles() {
        Map<String, ObjectNode> styles = new HashMap<>();
        NodeList styleList = document.getElementsByTagName("style");
        for (int i = 0; i < styleList.getLength(); i++) {
            Element style = (Element) styleList.item(i);
            Iterator<Map.Entry<String, JsonNode>> iterator = Json.parse(style.getTextContent()).fields();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> e = iterator.next();
                styles.put(e.getKey(), (ObjectNode) e.getValue());
            }
        }
        return styles;
    }
}
