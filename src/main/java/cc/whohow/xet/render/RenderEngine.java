package cc.whohow.xet.render;

import com.fasterxml.jackson.databind.JsonNode;

public interface RenderEngine<CONTEXT> {
    void render(CONTEXT context, JsonNode node);
}
