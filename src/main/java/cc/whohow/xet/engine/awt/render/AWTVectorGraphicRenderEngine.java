package cc.whohow.xet.engine.awt.render;

import cc.whohow.xet.engine.awt.AWTXetContext;
import cc.whohow.xet.model.Styles;
import cc.whohow.xet.render.RenderEngine;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.awt.*;

public abstract class AWTVectorGraphicRenderEngine<CONTEXT extends AWTXetContext> implements RenderEngine<CONTEXT> {
    @Override
    public void render(CONTEXT context, JsonNode node) {
        ObjectNode style = Styles.getComputedStyle(node);

        Graphics2D g = context.getGraphics();
        Shape shape = getShape(context, node);

        Stroke stroke = getStroke(context, style);
        if (stroke != null) {
            Color strokeColor = getStrokeColor(context, style);
            g.setStroke(stroke);
            g.setColor(strokeColor);
            g.draw(shape);
        }

        Color fill = getFillColor(context, style);
        if (fill != null) {
            g.setColor(fill);
            g.fill(shape);
        }
    }

    protected Stroke getStroke(CONTEXT context, ObjectNode style) {
        int strokeWidth = Styles.STROKE_WIDTH.getInt(style, 1);
        if (strokeWidth == 0) {
            return null;
        }
        return new BasicStroke(Styles.STROKE_WIDTH.getInt(style, 1));
    }

    protected Color getStrokeColor(CONTEXT context, ObjectNode style) {
        return context.getColor(Styles.STROKE.get(style, "#ffffff"));
    }

    protected Color getFillColor(CONTEXT context, ObjectNode style) {
        String fill = Styles.FILL.get(style);
        if (fill == null || fill.isEmpty()) {
            return null;
        }
        return context.getColor(fill);
    }

    protected abstract Shape getShape(CONTEXT context, JsonNode node);
}
