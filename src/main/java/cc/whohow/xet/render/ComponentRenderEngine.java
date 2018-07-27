package cc.whohow.xet.render;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

public class ComponentRenderEngine<CONTEXT> implements RenderEngine<CONTEXT> {
    protected Map<String, RenderEngine<CONTEXT>> engines = new HashMap<>();

    public void addRenderEngine(String tagName, RenderEngine<CONTEXT> engine) {
        this.engines.put(tagName, engine);
    }

    @Override
    public void render(CONTEXT context, JsonNode node) {
        RenderEngine<CONTEXT> renderEngine = getRenderEngine(context, node);
        if (renderEngine != null) {
            renderEngine.render(context, node);
        } else {
            JsonNode children = node.path("children");
            for (JsonNode child : children) {
                render(context, child);
            }
        }
    }

    protected RenderEngine<CONTEXT> getRenderEngine(CONTEXT context, JsonNode node) {
        return engines.get(node.path("tagName").textValue());
    }
}
