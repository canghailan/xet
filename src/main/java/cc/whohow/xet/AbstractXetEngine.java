package cc.whohow.xet;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URL;

public abstract class AbstractXetEngine<T> implements XetEngine<T> {
    private static final DocumentBuilderFactory FACTORY = DocumentBuilderFactory.newInstance();

    public T process(URL document) {
        return process(parse(document));
    }

    public T process(String document) {
        return process(parse(document));
    }

    public T process(InputStream document) {
        return process(parse(document));
    }

    protected Document parse(URL url) {
        try (InputStream stream = url.openStream()) {
            return parse(stream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    protected Document parse(String xml) {
        try {
            return FACTORY.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (SAXException | ParserConfigurationException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    protected Document parse(InputStream stream) {
        try {
            return FACTORY.newDocumentBuilder().parse(stream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (SAXException | ParserConfigurationException e) {
            throw new UndeclaredThrowableException(e);
        }
    }
}
