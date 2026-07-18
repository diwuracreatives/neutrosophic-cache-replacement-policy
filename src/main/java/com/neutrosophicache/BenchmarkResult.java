package com.neutrosophicache;

import java.util.List;

public record BenchmarkResult(String policy, double hitRate, double missRate, List<String> evictionLog) {

    @Override
    public String toString() {
        return String.format(
                "%-15s | Hit Rate: %5.1f%% | Miss Rate: %5.1f%%",
                policy, hitRate, missRate);
    }
}