package cc.whohow.xet.layout;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.function.Function;

public class DefaultLayoutEngine implements TreeLayoutEngine {
    private final Function<JsonNode, LayoutEngine> layoutEngineFactory;

    public DefaultLayoutEngine(Function<JsonNode, LayoutEngine> layoutEngineFactory) {
        this.layoutEngineFactory = layoutEngineFactory;
    }

    @Override
    public void layout(JsonNode node) {
        LayoutEngine engine = layoutEngineFactory.apply(node);
        if (engine != null) {
            engine.layout(node);
        } else {
            for (JsonNode child : node.path("children")) {
                layout(node, child);
            }
        }
    }

    @Override
    public void layout(JsonNode parent, JsonNode node) {
        layout(node);
    }
}
