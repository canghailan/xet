package cc.whohow.xet.engine.awt.render;

import cc.whohow.xet.engine.awt.AWTXetContext;
import cc.whohow.xet.model.Styles;
import cc.whohow.xet.util.Points;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.awt.*;

public class AWTPolylineRenderEngine<CONTEXT extends AWTXetContext> extends AWTPolygonRenderEngine<CONTEXT> {
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
        g.drawPolyline(xPoints, yPoints, nPoints);
    }
}
