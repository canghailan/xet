package cc.whohow.xet.layout;

import com.fasterxml.jackson.databind.JsonNode;

public class NoopLayoutEngine<CONTEXT> implements LayoutEngine<CONTEXT> {
    @Override
    public void layout(CONTEXT context, JsonNode node) {
    }
}
