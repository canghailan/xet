package cc.whohow.xet.engine.awt.render;

import cc.whohow.xet.engine.awt.AWTXetContext;
import cc.whohow.xet.model.Styles;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.awt.geom.Ellipse2D;

public class AWTCircleRenderEngine<CONTEXT extends AWTXetContext> extends AWTVectorGraphicRenderEngine<CONTEXT> {
    @Override
    protected Ellipse2D getShape(CONTEXT context, JsonNode node) {
        JsonNode props = node.path("props");
        int r = props.path("r").asInt();

        ObjectNode style = Styles.getComputedStyle(node);
        int x = Styles.X.getInt(style, 0);
        int y = Styles.Y.getInt(style, 0);
        return new Ellipse2D.Float(x, y, r * 2, r * 2);
    }
}
