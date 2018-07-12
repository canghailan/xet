package cc.whohow.xet.context;

import java.awt.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorFactory implements Function<String, Color> {
    private static final Pattern HEX = Pattern.compile("#(?<r>[0-9a-f]{2})(?<g>[0-9a-f]{2})(?<b>[0-9a-f]{2})", Pattern.CASE_INSENSITIVE);

    @Override
    public Color apply(String color) {
        Matcher hex = HEX.matcher(color);
        if (hex.matches()) {
            int r = Integer.parseInt(hex.group("r"), 16);
            int g = Integer.parseInt(hex.group("g"), 16);
            int b = Integer.parseInt(hex.group("b"), 16);
            return new Color(r, g, b);
        }
        throw new IllegalArgumentException(color);
    }
}
