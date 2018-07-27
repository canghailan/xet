package cc.whohow.xet.layout;

import com.fasterxml.jackson.databind.JsonNode;

public interface TreeLayoutEngine extends LayoutEngine {
    void layout(JsonNode parent, JsonNode node);
}
