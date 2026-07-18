package com.neutrosophicache;

import java.util.*;

public class WorkloadGenerator {
    private final Random random = new Random(42);
    private final int itemSpace;
    private final int requestCount;

    public WorkloadGenerator(int itemSpace, int requestCount) {
        this.itemSpace = itemSpace;
        this.requestCount = requestCount;
    }

    public List<String> generateContradictionWorkload() {
        List<String> requests = new ArrayList<>();
        for (int i = 0; i < itemSpace; i++) {
            requests.add("item_" + i);
        }

        int[] hotItems = {0, 1, 2, 3, 4};
        for (int i = 0; i < requestCount; i++) {
            if (random.nextDouble() < 0.8) {
                requests.add("item_" +
                        hotItems[random.nextInt(hotItems.length)]);
            } else {
                requests.add("item_" + random.nextInt(itemSpace));
            }
        }
        return requests;
    }


    public List<String> generateIgnoranceWorkload() {
        List<String> requests = new ArrayList<>();

        List<Integer> items = new ArrayList<>();
        for (int i = 0; i < itemSpace; i++) items.add(i);

        for (int i = 0; i < requestCount; i++) {
            Collections.shuffle(items, random);
            requests.add("item_" + items.get(
                    random.nextInt(itemSpace)));
        }
        return requests;
    }
    public List<String> generateMixedWorkload() {
        List<String> contradiction = generateContradictionWorkload();
        List<String> ignorance = generateIgnoranceWorkload();
        List<String> mixed = new ArrayList<>();

        for (int i = 0; i < Math.min(
                contradiction.size(), ignorance.size()); i++) {
            mixed.add(contradiction.get(i));
            mixed.add(ignorance.get(i));
        }
        return mixed;
    }

    public List<String> getLiveExampleTrace(List<String> workload) {
        return workload.subList(0, Math.min(30, workload.size()));
    }

    private static final String[] MATHS_TERMS = {
            "integral", "derivative", "matrix", "limit",
            "eigenvalue", "neutrosophic", "fourier_transform",
            "riemann_surface", "fifo", "topology",
            "determinant", "vector", "calculus", "algebra",
            "probability"
    };

    public List<String> generateLiveDemoTrace() {

        String[] sequence = {
                "integral",
                "derivative",
                "matrix",
                "integral",
                "limit",
                "eigenvalue",
                "integral",
                "derivative",
                "neutrosophic",
                "fourier_transform",
                "integral",
                "matrix",
                "riemann_surface",
                "derivative",
                "limit",
                "integral",
                "eigenvalue",
                "matrix",
                "neutrosophic",
                "integral",
                "topology",
                "derivative",
                "integral",
                "fifo",
                "matrix",
                "integral",
                "derivative",
                "limit",
                "integral",
                "matrix"
        };

        return Arrays.asList(sequence);
    }
}