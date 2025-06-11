package ru.natali.medregistry.config;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import jakarta.annotation.PostConstruct;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.concurrent.ConcurrentMap;

@Configuration
public class CacheMetricsConfig {
//это сделано, чтобы посмотреть кэш в актуаторе, не показывал cache.size
    private final CacheManager cacheManager;

    public CacheMetricsConfig(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @PostConstruct
    public void registerCacheMetrics() {
        cacheManager.getCacheNames().forEach(cacheName -> {
            var cache = cacheManager.getCache(cacheName);
            if (cache instanceof ConcurrentMapCache concurrentMapCache) {
                registerCacheSizeMetric(cacheName, concurrentMapCache);
            }
        });
    }

    private void registerCacheSizeMetric(String cacheName, ConcurrentMapCache cache) {
        ConcurrentMap<Object, Object> nativeCache = cache.getNativeCache();

        Metrics.gauge("cache.size",
                Collections.singletonList(Tag.of("cache", cacheName)),
                nativeCache,
                ConcurrentMap::size
        );
    }
}