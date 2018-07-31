package cc.whohow.xet.layout;

import cc.whohow.xet.model.Styles;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class AbstractImageLayoutEngine<CONTEXT, IMAGE> implements ImageLayoutEngine<CONTEXT, IMAGE> {
    @Override
    public void layout(CONTEXT context, JsonNode node) {
        ObjectNode style = Styles.getComputedStyle(node);
        if (Styles.WIDTH.isNull(style) || Styles.HEIGHT.isNull(style)) {
            IMAGE image = getImage(context, node);
            Styles.WIDTH.setInt(style, getWidth(image));
            Styles.HEIGHT.setInt(style, getHeight(image));
        }
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
