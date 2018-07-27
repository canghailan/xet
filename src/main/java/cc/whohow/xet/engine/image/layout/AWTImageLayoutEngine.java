package cc.whohow.xet.engine.image.layout;

import cc.whohow.xet.layout.AbstractImageLayoutEngine;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.awt.*;
import java.util.function.Function;

public class AWTImageLayoutEngine extends AbstractImageLayoutEngine {
    private final Function<String, Image> imageFactory;

    public AWTImageLayoutEngine(Function<String, Image> imageFactory) {
        this.imageFactory = imageFactory;
    }

    @Override
    protected void setImageResolution(String src, ObjectNode style) {
        Image image = imageFactory.apply(src);
        style.put("width", image.getWidth(null));
        style.put("height", image.getHeight(null));
    }
}
