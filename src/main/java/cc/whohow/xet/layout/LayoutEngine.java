package cc.whohow.xet.layout;

import com.fasterxml.jackson.databind.JsonNode;

public interface LayoutEngine<CONTEXT> {
    void layout(CONTEXT context, JsonNode node);
}
