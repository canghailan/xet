package cc.whohow.xet.engine.awt.render;

import cc.whohow.xet.engine.awt.AWTXetContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.awt.geom.Ellipse2D;

public class AWTEllipseRenderEngine<CONTEXT extends AWTXetContext> extends AWTVectorGraphicRenderEngine<CONTEXT> {
    @Override
    protected Ellipse2D getShape(CONTEXT context, JsonNode node) {
        JsonNode props = node.path("props");
        int cx = props.path("cx").asInt();
        int cy = props.path("cy").asInt();
        int rx = props.path("rx").asInt();
        int ry = props.path("ry").asInt();
        return new Ellipse2D.Float(cx, cy, rx * 2, ry * 2);
    }
}
