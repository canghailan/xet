package cc.whohow.xet.layout;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class AbstractImageLayoutEngine<CONTEXT, IMAGE> implements ImageLayoutEngine<CONTEXT, IMAGE> {
    @Override
    public void layout(CONTEXT context, JsonNode node) {
        ObjectNode style = getComputedStyle(node);
        JsonNode width = style.path("width");
        JsonNode height = style.path("height");
        if (width.isMissingNode() || width.isNull() ||
                height.isMissingNode() || height.isNull()) {
            IMAGE image = getImage(context, node);
            style.put("width", getWidth(image));
            style.put("height", getHeight(image));
        }
    }

    protected ObjectNode getComputedStyle(JsonNode node) {
        return (ObjectNode) node.path("computedStyle");
    }

    protected String getSrc(JsonNode node) {
        return node.path("props").path("src").textValue();
    }

    @Override
    public IMAGE getImage(CONTEXT context, JsonNode node) {
        return getImage(context, getSrc(node));
    }

    protected abstract IMAGE getImage(CONTEXT context, String src);
}
