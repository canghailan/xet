package cc.whohow.xet.engine.awt.render;

import cc.whohow.xet.engine.awt.AWTXetContext;
import cc.whohow.xet.model.Styles;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.awt.geom.Rectangle2D;

public class AWTRectRenderEngine<CONTEXT extends AWTXetContext> extends AWTVectorGraphicRenderEngine<CONTEXT> {
    @Override
    protected Rectangle2D getShape(CONTEXT context, JsonNode node) {
        JsonNode props = node.path("props");
        int width = props.path("width").asInt();
        int height = props.path("height").asInt();

        ObjectNode style = Styles.getComputedStyle(node);
        int x = Styles.X.getInt(style, 0);
        int y = Styles.Y.getInt(style, 0);
        return new Rectangle2D.Float(x, y, width, height);
    }
}
