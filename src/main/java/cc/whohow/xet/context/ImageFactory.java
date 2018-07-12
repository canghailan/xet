package cc.whohow.xet.context;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.function.Function;

public class ImageFactory implements Function<String, Image> {
    @Override
    public Image apply(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
