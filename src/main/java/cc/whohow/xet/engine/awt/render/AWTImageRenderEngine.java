package cc.whohow.xet.engine.awt.render;

import cc.whohow.xet.engine.awt.AWTXetContext;
import cc.whohow.xet.model.Styles;
import cc.whohow.xet.render.RenderEngine;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.awt.*;

public class AWTImageRenderEngine<CONTEXT extends AWTXetContext> implements RenderEngine<CONTEXT> {
    @Override
    public void render(CONTEXT context, JsonNode imageNode) {
        JsonNode props = imageNode.path("props");
        String src = props.path("src").textValue();

        ObjectNode style = Styles.getComputedStyle(imageNode);
        int x = Styles.X.getInt(style);
        int y = Styles.Y.getInt(style);
        int width = Styles.WIDTH.getInt(style);
        int height = Styles.HEIGHT.getInt(style);

        Image image = context.getImage(src);

        Graphics g = context.getGraphics();
        g.drawImage(image, x, y, width, height, null);
    }
}
