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

        Points points = getPoints(context, node);
        int nPoints = points.size();
        int[] xPoints = points.getX();
        int[] yPoints = points.getY();

        Graphics2D g = context.getGraphics();
        g.setStroke(getStroke(context, style));
        g.setColor(getStrokeColor(context, style));
        g.drawPolygon(xPoints, yPoints, nPoints);

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
        Points points = getPoints(context, node);
        int nPoints = points.size();
        int[] xPoints = points.getX();
        int[] yPoints = points.getY();
        return new Polygon(xPoints, yPoints, nPoints);
    }
}
