package cc.whohow.xet.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class CacheableFactory<T, R> implements Function<T, R> {
    private final Map<T, R> cache = new ConcurrentHashMap<>();
    private final Function<T, R> delegate;

    public CacheableFactory(Function<T, R> delegate) {
        this.delegate = delegate;
    }

    @Override
    public R apply(T t) {
        return cache.computeIfAbsent(t, delegate);
    }
}
