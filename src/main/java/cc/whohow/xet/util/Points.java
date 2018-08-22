package cc.whohow.xet.util;

import java.util.regex.Pattern;

public class Points {
    private static final Pattern D = Pattern.compile("\\D+");

    private final int[] xy;

    public Points(int[] xy) {
        this.xy = xy;
    }

    public Points(CharSequence xy) {
        this.xy = D.splitAsStream(xy).mapToInt(Integer::parseInt).toArray();
    }

    public int size() {
        return xy.length / 2;
    }

    public int[] getX() {
        int[] x = new int[size()];
        for (int i = 0; i < x.length; i++) {
            x[i] = xy[i * 2];
        }
        return x;
    }

    public int[] getY() {
        int[] y = new int[size()];
        for (int i = 0; i < y.length; i++) {
            y[i] = xy[i * 2 + 1];
        }
        return y;
    }
}
