package cc.whohow.xet.engine.awt.render;

import cc.whohow.xet.engine.awt.AWTXetContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.awt.geom.Line2D;

public class AWTLineRenderEngine<CONTEXT extends AWTXetContext> extends AWTVectorGraphicRenderEngine<CONTEXT> {
    @Override
    protected Line2D getShape(CONTEXT context, JsonNode node) {
        JsonNode props = node.path("props");
        int x1 = props.path("x1").asInt();
        int y1 = props.path("y1").asInt();
        int x2 = props.path("x2").asInt();
        int y2 = props.path("y2").asInt();
        return new Line2D.Float(x1, y1, x2, y2);
    }
}