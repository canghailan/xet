package cc.whohow.xet.context;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URL;
import java.util.function.Function;

public class FontFactory implements Function<String, Font> {
    @Override
    public Font apply(String url) {
        try (InputStream stream = new URL(url).openStream()) {
            return Font.createFont(Font.TRUETYPE_FONT, stream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (FontFormatException e) {
            throw new UndeclaredThrowableException(e);
        }
    }
}
