package cc.whohow.xet.engine.image.render;

import cc.whohow.xet.engine.image.ImageXetContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class DefaultRender implements BiConsumer<ImageXetContext, JsonNode> {
    private Map<String, BiConsumer<ImageXetContext, JsonNode>> renders = new HashMap<>();

    public void addRender(String tagName, BiConsumer<ImageXetContext, JsonNode> render) {
        renders.put(tagName, render);
    }

    @Override
    public void accept(ImageXetContext context, JsonNode node) {
        String tagName = node.path("tagName").textValue();
        BiConsumer<ImageXetContext, JsonNode> render = renders.get(tagName);
        if (render != null) {
            render.accept(context, node);
        } else {
            renderChildren(context, node);
        }
    }

    public void renderChildren(ImageXetContext context, JsonNode node) {
        JsonNode children = node.path("children");
        for (JsonNode child : children) {
            accept(context, child);
        }
    }
}
