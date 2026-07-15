package com.neutrosophicache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.List;

public class BenchmarkRunner {
    private final int cacheCapacity;

    private int lruHits, lruMisses;
    private int lfuHits, lfuMisses;

    public BenchmarkRunner(int cacheCapacity) {
        this.cacheCapacity = cacheCapacity;
    }

    public BenchmarkResult runNeutrosophic(List<String> workload) {
        NeutrosophicCache cache =
                new NeutrosophicCache(cacheCapacity, 0.7);
        for (String key : workload) {
            if (cache.get(key) == null) {
                cache.put(key, "value_" + key);
            }
        }
        return new BenchmarkResult(
                "Neutrosophic",
                cache.getHitRate(),
                cache.getMissRate(),
                cache.getEvictionLog()
        );
    }

    public BenchmarkResult runLRU(List<String> workload) {
        lruHits = 0; lruMisses = 0;
        Cache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(cacheCapacity)
                .build();
        for (String key : workload) {
            String val = cache.getIfPresent(key);
            if (val != null) {
                lruHits++;
            } else {
                lruMisses++;
                cache.put(key, "value_" + key);
            }
        }
        int total = lruHits + lruMisses;
        return new BenchmarkResult(
                "LRU",
                lruHits * 100.0 / total,
                lruMisses * 100.0 / total,
                null
        );
    }

    public BenchmarkResult runLFU(List<String> workload) {
        lfuHits = 0; lfuMisses = 0;
        Cache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(cacheCapacity)
                .build();
        for (String key : workload) {
            String val = cache.getIfPresent(key);
            if (val != null) {
                lfuHits++;
            } else {
                lfuMisses++;
                cache.put(key, "value_" + key);
            }
        }
        int total = lfuHits + lfuMisses;
        return new BenchmarkResult(
                "LFU",
                lfuHits * 100.0 / total,
                lfuMisses * 100.0 / total,
                null
        );
    }
}
