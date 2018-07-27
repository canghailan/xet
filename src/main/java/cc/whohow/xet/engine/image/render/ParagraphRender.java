package cc.whohow.xet.engine.image.render;

import cc.whohow.xet.engine.image.ImageXetContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.awt.*;
import java.util.function.BiConsumer;

public class ParagraphRender implements BiConsumer<ImageXetContext, JsonNode> {
    @Override
    public void accept(ImageXetContext context, JsonNode p) {
        Graphics g = context.getGraphics();
        for (JsonNode node : p.path("textLayout")) {
            JsonNode style = node.path("computedStyle");
            String text = node.path("text").textValue();
            int x = style.path("x").intValue();
            int y = style.path("y").intValue();
            String color = style.path("color").textValue();
            String fontFamily = style.path("color").textValue();
            int fontSize = style.path("color").intValue();

            g.setColor(context.getColor(color));
            g.setFont(context.getFont(fontFamily, fontSize));
            g.drawString(text, x, y);
        }
    }
}
