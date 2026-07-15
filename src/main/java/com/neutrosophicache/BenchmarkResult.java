package com.neutrosophicache;

import java.util.List;

public class BenchmarkResult {
    public final String policy;
    public final double hitRate;
    public final double missRate;
    public final List<String> evictionLog;

    public BenchmarkResult(String policy, double hitRate,
                           double missRate, List<String> evictionLog) {
        this.policy = policy;
        this.hitRate = hitRate;
        this.missRate = missRate;
        this.evictionLog = evictionLog;
    }

    @Override
    public String toString() {
        return String.format(
                "%-15s | Hit Rate: %5.1f%% | Miss Rate: %5.1f%%",
                policy, hitRate, missRate);
    }
}