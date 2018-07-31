package cc.whohow.xet.engine.awt.render;

import cc.whohow.xet.engine.awt.AWTXetContext;
import cc.whohow.xet.model.FontMeta;
import cc.whohow.xet.model.Styles;
import cc.whohow.xet.render.RenderEngine;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.awt.*;

public class AWTTextRenderEngine<CONTEXT extends AWTXetContext> implements RenderEngine<CONTEXT> {
    @Override
    public void render(CONTEXT context, JsonNode textNode) {
        Graphics g = context.getGraphics();
        for (JsonNode node : textNode.path("textLayout")) {
            String text = node.path("text").textValue();

            ObjectNode style = Styles.getComputedStyle(node);
            int x = Styles.X.getInt(style);
            int y = Styles.Y.getInt(style);
            String color = Styles.COLOR.get(style);
            FontMeta fontMeta = new FontMeta.Builder()
                    .withStyle(style)
                    .get();

            g.setColor(context.getColor(color));
            g.setFont(context.getFont(fontMeta));
            g.drawString(text, x, y);
        }
    }
}
