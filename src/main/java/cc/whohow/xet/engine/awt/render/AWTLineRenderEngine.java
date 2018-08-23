package cc.whohow.xet.engine.awt.render;

import cc.whohow.xet.engine.awt.AWTXetContext;
import cc.whohow.xet.model.Styles;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.awt.geom.Line2D;

public class AWTLineRenderEngine<CONTEXT extends AWTXetContext> extends AWTVectorGraphicRenderEngine<CONTEXT> {
    @Override
    protected Line2D getShape(CONTEXT context, JsonNode node) {
        JsonNode props = node.path("props");
        int x1 = props.path("x1").asInt();
        int y1 = props.path("y1").asInt();
        int x2 = props.path("x2").asInt();
        int y2 = props.path("y2").asInt();

        ObjectNode style = Styles.getComputedStyle(node);
        int x = Styles.X.getInt(style, 0);
        int y = Styles.Y.getInt(style, 0);
        return new Line2D.Float(x + x1, y + y1, x + x2, y + y2);
    }
}