package cc.whohow.xet.context;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface XetContext {
    Document getDocument();

    ObjectNode getStyle(Element element);

    ObjectNode getComputedStyle(Element element);
}
