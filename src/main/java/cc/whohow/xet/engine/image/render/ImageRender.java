package cc.whohow.xet.engine.image.render;

import cc.whohow.xet.engine.image.ImageXetContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.awt.*;
import java.util.function.BiConsumer;

public class ImageRender implements BiConsumer<ImageXetContext, JsonNode> {
    @Override
    public void accept(ImageXetContext context, JsonNode img) {
        JsonNode props = img.path("props");
        JsonNode style = img.path("computedStyle");

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
