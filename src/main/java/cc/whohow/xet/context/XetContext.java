package cc.whohow.xet.context;

import com.fasterxml.jackson.databind.JsonNode;
import org.w3c.dom.Document;

public interface XetContext {
    Document getDocument();

    JsonNode getVirtualDOM();
}
