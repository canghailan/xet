package cc.whohow.xet.layout;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

public class ComponentLayoutEngine<CONTEXT> implements LayoutEngine<CONTEXT> {
    protected Map<String, LayoutEngine<CONTEXT>> engines = new HashMap<>();

    public void addLayoutEngine(String tagName, LayoutEngine<CONTEXT> engine) {
        this.engines.put(tagName, engine);
    }

    @Override
    public void layout(CONTEXT context, JsonNode node) {
        LayoutEngine<CONTEXT> engine = getLayoutEngine(context, node);
        if (engine != null) {
            engine.layout(context, node);
        } else {
            for (JsonNode child : node.path("children")) {
                layout(context, node, child);
            }
        }
    }

    protected LayoutEngine<CONTEXT> getLayoutEngine(CONTEXT context, JsonNode node) {
        return engines.get(node.path("tagName").textValue());
    }

    protected void layout(CONTEXT context, JsonNode parent, JsonNode node) {
        layout(context, node);
    }
}
