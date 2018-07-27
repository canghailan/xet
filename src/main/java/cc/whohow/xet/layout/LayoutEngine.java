package cc.whohow.xet.layout;

import com.fasterxml.jackson.databind.JsonNode;

public interface LayoutEngine {
    void layout(JsonNode node);
}
