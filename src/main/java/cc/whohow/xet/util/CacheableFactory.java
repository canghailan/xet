package cc.whohow.xet.util;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class CacheableFactory<T, R> implements Function<T, R> {
    private final LoadingCache<T, R> cache;
    private final Function<T, R> delegate;

    public CacheableFactory(Function<T, R> delegate) {
        this.cache = Caffeine.newBuilder()
                .maximumSize(16)
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .weakValues()
                .build(delegate::apply);
        this.delegate = delegate;
    }

    @Override
    public R apply(T t) {
        return cache.get(t);
    }
}
