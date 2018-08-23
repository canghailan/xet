package cc.whohow.xet.engine.awt.render;

import cc.whohow.xet.engine.awt.AWTXetContext;
import cc.whohow.xet.model.Styles;
import cc.whohow.xet.util.Points;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.awt.*;

public class AWTPolygonRenderEngine<CONTEXT extends AWTXetContext> extends AWTVectorGraphicRenderEngine<CONTEXT> {
    @Override
    public void render(CONTEXT context, JsonNode node) {
        ObjectNode style = Styles.getComputedStyle(node);
        int x = Styles.X.getInt(style, 0);
        int y = Styles.Y.getInt(style, 0);

        Points points = getPoints(context, node).translate(x, y);
        int nPoints = points.size();
        int[] xPoints = points.getX();
        int[] yPoints = points.getY();

        Graphics2D g = context.getGraphics();
        Stroke stroke = getStroke(context, style);
        if (stroke != null) {
            Color strokeColor = getStrokeColor(context, style);
            g.setStroke(stroke);
            g.setColor(strokeColor);
            g.drawPolygon(xPoints, yPoints, nPoints);
        }

        Color fill = getFillColor(context, style);
        if (fill != null) {
            g.setColor(fill);
            g.fillPolygon(xPoints, yPoints, nPoints);
        }
    }

    protected Points getPoints(CONTEXT context, JsonNode node) {
        JsonNode props = node.path("props");
        String points = props.path("points").textValue();
        return new Points(points);
    }

    @Override
    protected Polygon getShape(CONTEXT context, JsonNode node) {
        ObjectNode style = Styles.getComputedStyle(node);
        int x = Styles.X.getInt(style, 0);
        int y = Styles.Y.getInt(style, 0);

        Points points = getPoints(context, node).translate(x, y);
        int nPoints = points.size();
        int[] xPoints = points.getX();
        int[] yPoints = points.getY();
        return new Polygon(xPoints, yPoints, nPoints);
    }
}
