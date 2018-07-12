package cc.whohow.xet;

import org.w3c.dom.Document;

public interface XetEngine<T> {
    T process(Document document);
}
