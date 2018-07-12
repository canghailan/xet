package cc.whohow.xet.engine.image.handler;

import cc.whohow.xet.box.ImageBox;
import cc.whohow.xet.engine.image.ImageXetContext;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.w3c.dom.Element;

import java.awt.*;
import java.util.function.BiConsumer;

public class ImageHandler implements BiConsumer<ImageXetContext, Element> {
    @Override
    public void accept(ImageXetContext context, Element element) {
        ObjectNode style = context.getComputedStyle(element);
        ImageBox imageBox = new ImageBox();
        imageBox.setSrc(element.getAttribute("src"));
        imageBox.setX(context.getX(style));
        imageBox.setY(context.getX(style));
        imageBox.setWidth(context.getWidth(style));
        imageBox.setHeight(context.getHeight(style));

        Image image = context.getImage(imageBox.getSrc());
        if (imageBox.getWidth() < 0) {
            imageBox.setWidth(image.getWidth(null));
        }
        if (imageBox.getHeight() < 0) {
            imageBox.setHeight(image.getHeight(null));
        }

        Graphics g = context.getGraphics();
        g.drawImage(image, imageBox.getX(), imageBox.getY(), imageBox.getWidth(), imageBox.getHeight(), null);
    }
}
