package cc.whohow.xet.engine.image.handler;

import cc.whohow.xet.engine.image.ImageXetContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.function.BiConsumer;

public class DelegateHandler implements BiConsumer<ImageXetContext, Element> {
    @Override
    public void accept(ImageXetContext context, Element element) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child instanceof Element) {
                context.accept((Element) child);
            }
        }
    }
}
