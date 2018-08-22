package cc.whohow.xet.engine.awt.render;

import cc.whohow.xet.engine.awt.AWTXetContext;
import cc.whohow.xet.model.Styles;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class AWTRectRenderEngine<CONTEXT extends AWTXetContext> extends AWTVectorGraphicRenderEngine<CONTEXT> {
    @Override
    public void render(CONTEXT context, JsonNode node) {
        ObjectNode style = Styles.getComputedStyle(node);

        Graphics2D g = context.getGraphics();
        Rectangle2D shape = getShape(context, style);

        g.setStroke(getStroke(context, style));
        g.setColor(getStrokeColor(context, style));
        g.draw(shape);

        Color fill = getFillColor(context, style);
        if (fill != null) {
            g.setColor(fill);
            g.fill(shape);
        }
    }

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
