package cc.whohow.xet.engine.awt.render;

import cc.whohow.xet.engine.awt.AWTXetContext;
import cc.whohow.xet.model.Styles;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.awt.geom.Ellipse2D;

public class AWTEllipseRenderEngine<CONTEXT extends AWTXetContext> extends AWTVectorGraphicRenderEngine<CONTEXT> {
    @Override
    protected Ellipse2D getShape(CONTEXT context, JsonNode node) {
        JsonNode props = node.path("props");
        int rx = props.path("rx").asInt();
        int ry = props.path("ry").asInt();

        ObjectNode style = Styles.getComputedStyle(node);
        int x = Styles.X.getInt(style, 0);
        int y = Styles.Y.getInt(style, 0);
        return new Ellipse2D.Float(x, y, rx * 2, ry * 2);
    }
}
