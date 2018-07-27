package cc.whohow.xet.engine.awt.layout;

import cc.whohow.xet.engine.awt.AWTXetContext;
import cc.whohow.xet.layout.AbstractImageLayoutEngine;
import com.fasterxml.jackson.databind.JsonNode;

import java.awt.*;
import java.util.function.Function;

public class AWTImageLayoutEngine<CONTEXT extends AWTXetContext> extends AbstractImageLayoutEngine<CONTEXT, Image> {
    @Override
    protected Image getImage(CONTEXT context, String src) {
        return context.getImage(src);
    }

    @Override
    public int getWidth(Image image) {
        return image.getWidth(null);
    }

    @Override
    public int getHeight(Image image) {
        return image.getHeight(null);
    }
}
