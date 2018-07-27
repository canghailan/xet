package cc.whohow.xet.engine.awt.render;

import cc.whohow.xet.engine.awt.AWTXetContext;
import cc.whohow.xet.render.RenderEngine;
import com.fasterxml.jackson.databind.JsonNode;

import java.awt.*;

public class AWTImageRenderEngine<CONTEXT extends AWTXetContext> implements RenderEngine<CONTEXT> {
    @Override
    public void render(CONTEXT context, JsonNode imageNode) {
        JsonNode props = imageNode.path("props");
        JsonNode style = imageNode.path("computedStyle");

        int x = style.path("x").intValue();
        int y = style.path("y").intValue();
        int width = style.path("width").intValue();
        int height = style.path("height").intValue();
        String src = props.path("src").textValue();

        Image image = context.getImage(src);

        Graphics g = context.getGraphics();
        g.drawImage(image, x, y, width, height, null);
    }
}
