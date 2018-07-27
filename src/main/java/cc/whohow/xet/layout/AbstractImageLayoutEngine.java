package cc.whohow.xet.layout;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class AbstractImageLayoutEngine implements LayoutEngine {
    @Override
    public void layout(JsonNode node) {
        ObjectNode style = (ObjectNode) node.path("computedStyle");
        JsonNode width = style.path("width");
        JsonNode height = style.path("height");
        if (width.isMissingNode() || width.isNull() ||
                height.isMissingNode() || height.isNull()) {
            String src = node.path("props").path("src").textValue();
            setImageResolution(src, style);
        }
    }

    protected abstract void setImageResolution(String src, ObjectNode style);
}
