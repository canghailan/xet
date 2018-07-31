package cc.whohow.xet.layout;

import cc.whohow.xet.model.Styles;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;
import java.util.Map;

public class ComponentLayoutEngine<CONTEXT> implements LayoutEngine<CONTEXT> {
    protected Map<String, LayoutEngine<CONTEXT>> engines = new HashMap<>();

    public void addLayoutEngine(String tagName, LayoutEngine<CONTEXT> engine) {
        this.engines.put(tagName, engine);
    }

    @Override
    public void layout(CONTEXT context, JsonNode node) {
        LayoutEngine<CONTEXT> engine = getLayoutEngine(context, node);
        if (engine != null) {
            engine.layout(context, node);
        } else {
            ObjectNode style = Styles.getComputedStyle(node);

            int y1 = 0;
            ObjectNode prevStyle = null;
            for (JsonNode child : node.path("children")) {
                ObjectNode childStyle = Styles.getComputedStyle(child);
                if (Styles.X.isNull(childStyle)) {
                    Styles.X.setInt(childStyle, calculateX(childStyle, style, prevStyle));
                }
                if (Styles.Y.isNull(childStyle)) {
                    Styles.Y.setInt(childStyle, calculateY(childStyle, style, prevStyle));
                }
                if (Styles.WIDTH.isNull(childStyle)) {
                    Styles.WIDTH.setInt(childStyle, calculateWidth(childStyle, style, prevStyle));
                }

                layout(context, node, child);

                y1 = Integer.max(y1, calculateY1(childStyle));
                prevStyle = childStyle;
            }
            if (Styles.HEIGHT.isNull(style)) {
                Styles.HEIGHT.setInt(style, y1 - Styles.Y.getInt(style));
            }
        }
    }

    protected LayoutEngine<CONTEXT> getLayoutEngine(CONTEXT context, JsonNode node) {
        return engines.get(node.path("tagName").textValue());
    }

    protected void layout(CONTEXT context, JsonNode parent, JsonNode node) {
        layout(context, node);
    }

    protected int calculateX0(ObjectNode style) {
        return Styles.X.getInt(style) -
                Styles.PADDING_LEFT.getInt(style) -
                Styles.BORDER_LEFT.getInt(style) -
                Styles.MARGIN_LEFT.getInt(style);
    }

    protected int calculateY0(ObjectNode style) {
        return Styles.Y.getInt(style) -
                Styles.PADDING_TOP.getInt(style) -
                Styles.BORDER_TOP.getInt(style) -
                Styles.MARGIN_TOP.getInt(style);
    }

    protected int calculateX1(ObjectNode style) {
        return Styles.X.getInt(style) +
                Styles.WIDTH.getInt(style) +
                Styles.PADDING_RIGHT.getInt(style) +
                Styles.BORDER_RIGHT.getInt(style) +
                Styles.MARGIN_RIGHT.getInt(style);
    }

    protected int calculateY1(ObjectNode style) {
        return Styles.Y.getInt(style) +
                Styles.HEIGHT.getInt(style) +
                Styles.PADDING_BOTTOM.getInt(style) +
                Styles.BORDER_BOTTOM.getInt(style) +
                Styles.MARGIN_BOTTOM.getInt(style);
    }

    protected int calculateX(ObjectNode style, ObjectNode parentStyle, ObjectNode previousSiblingStyle) {
        return Styles.X.getInt(parentStyle) +
                Styles.MARGIN_LEFT.getInt(style) +
                Styles.BORDER_LEFT.getInt(style) +
                Styles.PADDING_LEFT.getInt(style);
    }

    protected int calculateY(ObjectNode style, ObjectNode parentStyle, ObjectNode previousSiblingStyle) {
        if (previousSiblingStyle == null) {
            return Styles.Y.getInt(parentStyle) +
                    Styles.MARGIN_TOP.getInt(style) +
                    Styles.BORDER_TOP.getInt(style) +
                    Styles.PADDING_TOP.getInt(style);
        } else {
            return Styles.Y.getInt(previousSiblingStyle) +
                    Styles.HEIGHT.getInt(previousSiblingStyle) +
                    Styles.PADDING_BOTTOM.getInt(previousSiblingStyle) +
                    Styles.BORDER_BOTTOM.getInt(previousSiblingStyle) +
                    Styles.MARGIN_BOTTOM.getInt(previousSiblingStyle) +
                    Styles.MARGIN_TOP.getInt(style) +
                    Styles.BORDER_TOP.getInt(style) +
                    Styles.PADDING_TOP.getInt(style);
        }
    }

    protected int calculateWidth(ObjectNode style, ObjectNode parentStyle, ObjectNode previousSiblingStyle) {
        return Styles.WIDTH.getInt(parentStyle) -
                Styles.MARGIN_LEFT.getInt(style) -
                Styles.MARGIN_RIGHT.getInt(style) -
                Styles.BORDER_LEFT.getInt(style) -
                Styles.BORDER_RIGHT.getInt(style) -
                Styles.PADDING_LEFT.getInt(style) -
                Styles.PADDING_RIGHT.getInt(style);
    }
}
