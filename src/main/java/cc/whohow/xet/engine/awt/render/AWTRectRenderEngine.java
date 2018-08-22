package cc.whohow.xet.engine.awt.render;

import cc.whohow.xet.engine.awt.AWTXetContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.awt.geom.Rectangle2D;

public class AWTRectRenderEngine<CONTEXT extends AWTXetContext> extends AWTVectorGraphicRenderEngine<CONTEXT> {
    @Override
    protected Rectangle2D getShape(CONTEXT context, JsonNode node) {
        JsonNode props = node.path("props");
        int x = props.path("x").asInt();
        int y = props.path("y").asInt();
        int width = props.path("width").asInt();
        int height = props.path("height").asInt();
        return new Rectangle2D.Float(x, y, width, height);
    }
}
