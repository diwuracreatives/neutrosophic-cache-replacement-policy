package com.neutrosophicache;

import java.util.ArrayList;
import java.util.List;

public class CacheItem {
    public String key;
    public String value;

    public double T;
    public double I1;
    public double I2;
    public double F;

    public double integralSum;

    public long lastAccess;
    public int frequency;
    public List<Long> accessIntervals;

    public double p;

    private static final double ALPHA = 1.0;
    private static final double LAMBDA = 0.1;
    private static final double BETA = 1.0;
    private static final double MU = 0.05;
    private static final double OMEGA = Math.PI;
    private static final double GAMMA = 1.0;
    private static final double DELTA = 1.0;
    private static final double RHO = 0.1;

    public CacheItem(String key, String value) {
        this.key = key;
        this.value = value;
        this.lastAccess = System.currentTimeMillis();
        this.frequency = 1;
        this.accessIntervals = new ArrayList<>();
        this.integralSum = 0.0;
        this.p = 1.5;
        this.T = ALPHA;
        this.I1 = 0.0;
        this.I2 = 0.0;
        this.F = 0.0;
    }

    public void onAccess(long now) {
        long dt = now - lastAccess;
        if (dt > 0) {
            accessIntervals.add(dt);
        }
        frequency++;
        lastAccess = now;
        updateComponents(dt);
        integralSum += (T + I1 + I2) * (dt / 1000.0);
    }

    private void updateComponents(long dt) {
        double t = dt / 1000.0; // convert to seconds

        T = ALPHA * Math.exp(-LAMBDA * t);

        double sinVal = Math.sin(OMEGA * t);
        I1 = BETA * sinVal * sinVal * Math.exp(-MU * t);

        p = estimateP();
        I2 = (t > 0) ? GAMMA / Math.pow(t, p) : GAMMA;

        F = DELTA * (1 - Math.exp(-RHO * t));
    }

    private double estimateP() {
        if (accessIntervals.size() < 2) return 1.5;

        double mean = accessIntervals.stream()
                .mapToLong(Long::longValue)
                .average().orElse(1.0);

        double variance = accessIntervals.stream()
                .mapToDouble(interval ->
                        Math.pow(interval - mean, 2))
                .average().orElse(0.0);


        double normalisedVariance = variance / (mean * mean + 1.0);

        return 2.0 / (1.0 + normalisedVariance);
    }

    public boolean isEvictionCandidate(double falsityThreshold) {
        return p > 1.0 && F >= falsityThreshold;
    }
}
