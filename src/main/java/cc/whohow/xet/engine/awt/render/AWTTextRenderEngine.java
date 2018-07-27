package cc.whohow.xet.engine.awt.render;

import cc.whohow.xet.engine.awt.AWTXetContext;
import cc.whohow.xet.engine.image.ImageXetContext;
import cc.whohow.xet.model.FontMeta;
import cc.whohow.xet.render.RenderEngine;
import com.fasterxml.jackson.databind.JsonNode;

import java.awt.*;
import java.util.function.BiConsumer;

public class AWTTextRenderEngine<CONTEXT extends AWTXetContext> implements RenderEngine<CONTEXT> {
    @Override
    public void render(CONTEXT context, JsonNode textNode) {
        Graphics g = context.getGraphics();
        for (JsonNode node : textNode.path("textLayout")) {
            JsonNode style = node.path("computedStyle");
            String text = node.path("text").textValue();
            int x = style.path("x").intValue();
            int y = style.path("y").intValue();
            String color = style.path("color").textValue();

            FontMeta fontMeta = new FontMeta.Builder()
                    .withStyle(style)
                    .get();

            g.setColor(context.getColor(color));
            g.setFont(context.getFont(fontMeta));
            g.drawString(text, x, y);
        }
    }
}
