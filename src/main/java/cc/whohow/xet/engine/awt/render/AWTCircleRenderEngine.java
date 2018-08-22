package cc.whohow.xet.engine.awt.render;

import cc.whohow.xet.engine.awt.AWTXetContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.awt.geom.Ellipse2D;

public class AWTCircleRenderEngine<CONTEXT extends AWTXetContext> extends AWTVectorGraphicRenderEngine<CONTEXT> {
    @Override
    protected Ellipse2D getShape(CONTEXT context, JsonNode node) {
        JsonNode props = node.path("props");
        int cx = props.path("cx").asInt();
        int cy = props.path("cy").asInt();
        int r = props.path("r").asInt();
        return new Ellipse2D.Float(cx, cy, r * 2, r * 2);
    }
}
