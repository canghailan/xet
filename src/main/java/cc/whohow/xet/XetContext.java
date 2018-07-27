package cc.whohow.xet;

import com.fasterxml.jackson.databind.JsonNode;
import org.w3c.dom.Document;

public interface XetContext {
    Document getDocument();

    JsonNode getRenderTree();

    void layout();

    void render();
}
