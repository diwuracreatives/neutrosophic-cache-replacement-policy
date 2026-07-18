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
    public long insertionTime;
    public int frequency;
    public List<Long> accessIntervals;

    public double p;

    private static final double ALPHA = 1.0;
    private static final double LAMBDA = 0.001;
    private static final double BETA = 1.0;
    private static final double MU = 0.001;
    private static final double OMEGA = Math.PI;
    private static final double GAMMA = 1.0;
    private static final double DELTA = 1.0;
    private static final double RHO = 0.005;

    public CacheItem(String key, String value, long simulatedTime) {
        this.key = key;
        this.value = value;
        this.lastAccess = simulatedTime;
        this.insertionTime = simulatedTime;
        this.frequency = 1;
        this.accessIntervals = new ArrayList<>();
        this.integralSum = 0.0;
        this.p = 1.5;
        this.T = ALPHA;
        this.I1 = 0.0;
        this.I2 = 0.0;
        this.F = 0.0;
    }

    public void onAccess(long simulatedTime) {
        long dt = simulatedTime - lastAccess;
        if (dt > 0) {
            accessIntervals.add(dt);
        }
        frequency++;
        lastAccess = simulatedTime;
        updateComponents(simulatedTime, dt);
        integralSum += (T + I1 + I2) * dt;
    }

    private void updateComponents(long simulatedTime, long dt) {
        double t = dt;

        T = ALPHA * Math.exp(-LAMBDA * t);

        double sinVal = Math.sin(OMEGA * t);
        I1 = BETA * sinVal * sinVal * Math.exp(-MU * t);

        p = estimateP(simulatedTime);

        I2 = (t > 0) ? GAMMA / Math.pow(Math.max(t, 1), p) : GAMMA;


        double timeSinceLastAccess = simulatedTime - lastAccess;

        double age = simulatedTime - insertionTime;
        F = DELTA * (1 - Math.exp(-RHO * age))
                * (frequency < 3 ? 1.0 : 0.3);
    }

    private double estimateP(long simulatedTime) {
        if (accessIntervals.size() < 2) {

            long age = simulatedTime - insertionTime;
            if (age > 50 && frequency > 3) {
                return 0.5;
            }
            return 1.5;
        }

        double mean = accessIntervals.stream()
                .mapToLong(Long::longValue)
                .average().orElse(1.0);

        double variance = accessIntervals.stream()
                .mapToDouble(interval ->
                        Math.pow(interval - mean, 2))
                .average().orElse(0.0);


        double cv = (mean > 0) ?
                Math.sqrt(variance) / mean : 0;


        long age = simulatedTime - insertionTime;
        boolean isContradictory = age > 100
                && frequency > 5
                && mean < 20;

        if (isContradictory) {
            return 0.5;
        }

        if (cv > 2.0) return 0.4;
        if (cv > 1.0) return 0.8;

        return 1.5;
    }

    public void updateFalsity(long simulatedTime) {
        double age = simulatedTime - insertionTime;

        double accessRate = (age > 0) ?
                (frequency / (double) age) * 100 : 0;
        F = DELTA * (1 - Math.exp(-RHO * age))
                * Math.exp(-accessRate);
    }

    public boolean isEvictionCandidate(double falsityThreshold) {
        return p > 1.0 && F >= falsityThreshold;
    }
}