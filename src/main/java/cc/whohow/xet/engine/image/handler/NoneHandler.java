package cc.whohow.xet.engine.image.handler;

import cc.whohow.xet.engine.image.ImageXetContext;
import org.w3c.dom.Element;

import java.util.function.BiConsumer;

public class NoneHandler implements BiConsumer<ImageXetContext, Element> {
    @Override
    public void accept(ImageXetContext context, Element element) {
    }
}
