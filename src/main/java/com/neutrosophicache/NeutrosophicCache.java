package com.neutrosophicache;

import java.util.*;

/**
 * NeutrosophicCache - A cache replacement policy based on
 * 2-refined neutrosophic integral-based utility functions.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 * @author Adediwura Boluwatife
 * @version 1.0.0
 * @see <a href="https://neutrosophicache.boluwatifeadediwura.xyz/">NeutrosophicCache Project Documentation</a>
 * @see <a href="https://www.researchgate.net/publication/412950618_A_Neutrosophic_Cache_Replacement_Policy_Using_Improper_Integral-Based_Utility_Functions?_tp=eyJjb250ZXh0Ijp7InBhZ2UiOiJwcm9maWxlIiwicHJldmlvdXNQYWdlIjoiaG9tZSIsInBvc2l0aW9uIjoicGFnZUNvbnRlbnQifX0">A Neutrosophic Cache Replacement Policy Using Integral-Based Utility Functions (2026)</a>
 */

public class NeutrosophicCache<K, V> {
    private final int capacity;
    private final Map<K, CacheItem<V>> store;
    private final double falsityThreshold;

    private int hits = 0;
    private int misses = 0;

    private long simulatedTime = 0;

    /**
     * Creates a {@code NeutrosophicCache} with the given capacity
     * and default falsity threshold.
     *
     * @param capacity maximum number of items the cache holds
     * @throws IllegalArgumentException if capacity is less
     *         than or equal to zero
     */
    public NeutrosophicCache(int capacity) {
        this(capacity, 0.7);
    }


    /**
     * Creates a {@code NeutrosophicCache}
     *
     * @param capacity maximum number of items the cache holds
     * @param falsityThreshold  Falsity threshold
     * @throws IllegalArgumentException if capacity <= 0 or
     *         falsityThreshold is not between 0.0 and 1.0
     */

      NeutrosophicCache(int capacity, double falsityThreshold) {
        if (capacity <= 0) {
            throw new IllegalArgumentException(
                    "Cache capacity must be greater than zero. " +
                            "Got: " + capacity);
        }
        if (falsityThreshold < 0.0 || falsityThreshold > 1.0) {
            throw new IllegalArgumentException(
                    "Falsity threshold must be between " +
                            "0.0 and 1.0. Got: " + falsityThreshold);
        }
        this.capacity = capacity;
        this.falsityThreshold = falsityThreshold;
        this.store = new LinkedHashMap<>();
    }

    /**
     * Retrieves a value from the cache.
     *
     * @param key the key to look up
     * @return the cached value
     * @throws NullPointerException if key is null
     */
    public V get(K key) {
        if (key == null) {
            throw new NullPointerException(
                    "Cache key must not be null");
        }
        simulatedTime++;
        if (store.containsKey(key)) {
            store.get(key).onAccess(simulatedTime);
            hits++;
            return store.get(key).value;
        }
        misses++;
        return null;
    }


    /**
     * Inserts a key-value pair into the cache.
     * @param key   the key to insert
     * @param value the value to cache
     * @throws NullPointerException if key or value is null
     */
    public void put(K key, V value) {
        if (key == null) {
            throw new NullPointerException(
                    "Cache key must not be null");
        }
        if (value == null) {
            throw new NullPointerException(
                    "Cache value must not be null");
        }

        if (store.containsKey(key)) {
            store.get(key).onAccess(simulatedTime);
            return;
        }
        if (store.size() >= capacity) {
            evict();
        }
        store.put(key, new CacheItem<>(
                key.toString(), value, simulatedTime));
    }

    /**
     * Returns true if the cache contains the given key.
     *
     * @param key the key to check
     * @return true if present
     * @throws NullPointerException if key is null
     */
    public boolean containsKey(K key) {
        if (key == null) {
            throw new NullPointerException(
                    "Cache key must not be null");
        }
        return store.containsKey(key);
    }

    /**
     * Returns the current number of items in the cache.
     *
     * @return current cache size
     */
    public int size() {
        return store.size();
    }

    /**
     * Evicts an item from the cache.
     *  @throws IllegalStateException if cache is empty
     */
    private void evict() {
        store.values().forEach(item ->
                item.updateFalsity(simulatedTime));

        List<Map.Entry<K, CacheItem<V>>> candidates =
                new ArrayList<>();
        for (Map.Entry<K, CacheItem<V>> entry :
                store.entrySet()) {
            if (entry.getValue()
                    .isEvictionCandidate(falsityThreshold)) {
                candidates.add(entry);
            }
        }

        K victimKey;

        if (!candidates.isEmpty()) {
            victimKey = candidates.stream()
                    .max(Comparator.comparingDouble(e -> e.getValue().integralSum))
                    .map(Map.Entry::getKey)
                    .orElseThrow();
        } else {
            victimKey = store.entrySet().stream()
                    .min(Comparator.comparingDouble(e -> {
                        CacheItem<V> item = e.getValue();
                        return (item.frequency * 10.0)
                                - (item.F * 100.0)
                                + (item.p * 5.0);
                    }))
                    .map(Map.Entry::getKey)
                    .orElseThrow(() -> new IllegalStateException("Cannot evict from an empty cache"));
        }

        store.remove(victimKey);
    }

    /**
     * Returns the hit rate as a percentage.
     *
     * @return hit rate percentage
     */
    public double getHitRate() {
        int total = hits + misses;
        return total == 0 ? 0 : (hits * 100.0 / total);
    }

    /**
     * Returns the miss rate as a percentage.
     *
     * @return miss rate percentage
     */
    public double getMissRate() {
        return 100.0 - getHitRate();
    }

    /**
     * Clears all items and the eviction log.
     */
    public void reset() {
        store.clear();
        hits = 0;
        misses = 0;
        simulatedTime = 0;
    }
}
