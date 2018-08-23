package cc.whohow.xet.util;

import java.util.Arrays;
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

    public int getX(int index) {
        return xy[index * 2];
    }

    public int getY(int index) {
        return xy[index * 2 + 1];
    }

    public int[] getX() {
        int[] x = new int[size()];
        for (int i = 0; i < x.length; i++) {
            x[i] = getX(i);
        }
        return x;
    }

    public int[] getY() {
        int[] y = new int[size()];
        for (int i = 0; i < y.length; i++) {
            y[i] = getY(i);
        }
        return y;
    }

    public Points translate(int x, int y) {
        if (x == 0 && y == 0) {
            return this;
        }
        int[] xy = Arrays.copyOf(this.xy, this.xy.length);
        for (int i = 0; i < xy.length; i += 2) {
            xy[i] += x;
            xy[i + 1] += y;
        }
        return new Points(xy);
    }
}
