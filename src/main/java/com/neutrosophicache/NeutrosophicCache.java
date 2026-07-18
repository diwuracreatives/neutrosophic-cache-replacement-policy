package com.neutrosophicache;

import java.util.*;

public class NeutrosophicCache {
    private final int capacity;
    private final Map<String, CacheItem> store;
    private final double falsityThreshold;

    private int hits = 0;
    private int misses = 0;

    private long simulatedTime = 0;

    private final List<String> evictionLog = new ArrayList<>();

    public NeutrosophicCache(int capacity, double falsityThreshold) {
        this.capacity = capacity;
        this.falsityThreshold = falsityThreshold;
        this.store = new LinkedHashMap<>();
    }

    public String get(String key) {
        simulatedTime++;
        if (store.containsKey(key)) {
            store.get(key).onAccess(simulatedTime);
            hits++;
            return store.get(key).value;
        }
        misses++;
        return null;
    }

    public void put(String key, String value) {
        if (store.containsKey(key)) {
            store.get(key).onAccess(simulatedTime);
            return;
        }
        if (store.size() >= capacity) {
            evict();
        }
        store.put(key, new CacheItem(key, value, simulatedTime));
    }

    private void evict() {
        store.values().forEach(item ->
                item.updateFalsity(simulatedTime));

        List<Map.Entry<String, CacheItem>> candidates =
                new ArrayList<>();
        for (Map.Entry<String, CacheItem> entry : store.entrySet()) {
            if (entry.getValue()
                    .isEvictionCandidate(falsityThreshold)) {
                candidates.add(entry);
            }
        }

        String victimKey;

        if (!candidates.isEmpty()) {
            victimKey = candidates.stream()
                    .max(Comparator.comparingDouble(
                            e -> e.getValue().integralSum))
                    .get().getKey();
        } else {
            victimKey = store.entrySet().stream()
                    .min(Comparator.comparingDouble(e -> {
                        CacheItem item = e.getValue();
                        return (item.frequency * 10.0)
                                - (item.F * 100.0)
                                + (item.p * 5.0);
                    }))
                    .get().getKey();
        }

        CacheItem victim = store.get(victimKey);
        evictionLog.add(String.format(
                "EVICT key=%s p=%.2f F=%.3f " +
                        "integralSum=%.4f freq=%d",
                victimKey, victim.p, victim.F,
                victim.integralSum, victim.frequency));
        store.remove(victimKey);
    }

    public double getHitRate() {
        int total = hits + misses;
        return total == 0 ? 0 : (hits * 100.0 / total);
    }

    public double getMissRate() {
        return 100.0 - getHitRate();
    }

    public List<String> getEvictionLog() {
        return evictionLog;
    }

    public void reset() {
        store.clear();
        hits = 0;
        misses = 0;
        simulatedTime = 0;
        evictionLog.clear();
    }
}