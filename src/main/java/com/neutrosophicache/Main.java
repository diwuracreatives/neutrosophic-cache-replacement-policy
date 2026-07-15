package com.neutrosophicache;

import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        int CACHE_CAPACITY = 100;
        int ITEM_SPACE = 500;
        int REQUEST_COUNT = 10000;

        WorkloadGenerator gen = new WorkloadGenerator(
                ITEM_SPACE, REQUEST_COUNT);
        BenchmarkRunner runner =
                new BenchmarkRunner(CACHE_CAPACITY);

        List<String> workloadA = gen.generateContradictionWorkload();
        List<String> workloadB = gen.generateIgnoranceWorkload();
        List<String> workloadC = gen.generateMixedWorkload();

        System.out.println("=== NEUTROSOPHIC CACHE BENCHMARK ===\n");

        System.out.println("--- Workload A: Contradiction (I1) ---");
        BenchmarkResult neutroA = runner.runNeutrosophic(workloadA);
        BenchmarkResult lruA    = runner.runLRU(workloadA);
        BenchmarkResult lfuA    = runner.runLFU(workloadA);
        System.out.println(neutroA);
        System.out.println(lruA);
        System.out.println(lfuA);

        System.out.println("\n--- Workload B: Ignorance (I2) ---");
        BenchmarkResult neutroB = runner.runNeutrosophic(workloadB);
        BenchmarkResult lruB    = runner.runLRU(workloadB);
        BenchmarkResult lfuB    = runner.runLFU(workloadB);
        System.out.println(neutroB);
        System.out.println(lruB);
        System.out.println(lfuB);

        System.out.println("\n--- Workload C: Mixed ---");
        BenchmarkResult neutroC = runner.runNeutrosophic(workloadC);
        BenchmarkResult lruC    = runner.runLRU(workloadC);
        BenchmarkResult lfuC    = runner.runLFU(workloadC);
        System.out.println(neutroC);
        System.out.println(lruC);
        System.out.println(lfuC);

        System.out.println("\n=== LIVE EXAMPLE TRACE (30 requests) ===");
        List<String> trace = gen.getLiveExampleTrace(workloadC);
        NeutrosophicCache liveCache =
                new NeutrosophicCache(5, 0.7);
        for (String key : trace) {
            String result = liveCache.get(key);
            if (result == null) {
                liveCache.put(key, "value_" + key);
                System.out.println("MISS → inserted: " + key);
            } else {
                System.out.println("HIT  → retained: " + key);
            }
        }
        System.out.println("\nEviction Log:");
        liveCache.getEvictionLog()
                .forEach(System.out::println);
        System.out.printf("%nFinal Hit Rate:  %.1f%%%n",
                liveCache.getHitRate());
        System.out.printf("Final Miss Rate: %.1f%%%n",
                liveCache.getMissRate());



        double[] neutroRates = {
                neutroA.hitRate,
                neutroB.hitRate,
                neutroC.hitRate
        };
        double[] lruRates = {
                lruA.hitRate,
                lruB.hitRate,
                lruC.hitRate
        };
        double[] lfuRates = {
                lfuA.hitRate,
                lfuB.hitRate,
                lfuC.hitRate
        };

        try {
            GraphGenerator.generateHitRateGraph(
                    neutroRates,
                    lruRates,
                    lfuRates
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}