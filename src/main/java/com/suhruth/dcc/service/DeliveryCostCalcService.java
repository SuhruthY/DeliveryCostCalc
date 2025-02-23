package com.suhruth.dcc.service;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class DeliveryCostCalcService {

    private static final Map<String, Map<String, Double>> WAREHOUSES = Map.of(
            "C1", Map.of("A", 3.0, "B", 2.0, "C", 8.0),
            "C2", Map.of("D", 12.0, "E", 25.0, "F", 15.0),
            "C3", Map.of("G", 0.5, "H", 1.0, "I", 2.0)
    );

    private static final Map<String, Double> DISTANCES = Map.of(
            "C1-L1", 3.0, "C2-L1", 2.5, "C3-L1", 2.0,
            "C1-C2", 4.0, "C2-C3", 3.0, "C1-C3", 5.0,
            "C2-C1", 4.0, "C3-C2", 3.0, "C3-C1", 5.0
    );

    public static double calculateCost(double weight, double distance) {
        if (weight <= 5) {
            return distance * 10;
        } else {
            double baseCost = distance * 10;
            double additionalWeight = weight - 5;
            double additionalCost = (Math.floor(additionalWeight / 5)) * 8 * distance;
            if (additionalWeight % 5 != 0) {
                additionalCost += 8 * distance;
            }
            return baseCost + additionalCost;
        }
    }

    public static double calculateTotalWeight(Map<String, Integer> order, Map<String, Double> warehouse) {
        double totalWeight = 0;
        for (Map.Entry<String, Integer> entry : order.entrySet()) {
            String product = entry.getKey();
            int quantity = entry.getValue();
            if (warehouse.containsKey(product)) {
                totalWeight += warehouse.get(product) * quantity;
            }
        }
        return totalWeight;
    }

    public static double findMinimumCost(Map<String, Integer> order) {
        double minCost = Double.MAX_VALUE;
        Set<String> centersUsed = new HashSet<>();

        for (String product : order.keySet()) {
            for (Map.Entry<String, Map<String, Double>> warehouseEntry : WAREHOUSES.entrySet()) {
                if (warehouseEntry.getValue().containsKey(product)) {
                    centersUsed.add(warehouseEntry.getKey());
                }
            }
        }

        List<String> centersList = new ArrayList<>(centersUsed);
        List<List<String>> permutations = generatePermutations(centersList);

        for (List<String> route : permutations) {
            double currentCost = 0;
            double currentWeight = 0;
            String currentLocation = null;

            for (String center : route) {
                double weight = calculateTotalWeight(order, WAREHOUSES.get(center));
                if (currentLocation == null) {
                    currentCost += calculateCost(weight, DISTANCES.get(center + "-L1"));
                    currentLocation = "L1";
                } else {
                    String distanceKey;
                    if (currentLocation.equals(center)) {
                        distanceKey = center + "-L1";
                    } else if (DISTANCES.containsKey(currentLocation + "-" + center)) {
                        distanceKey = currentLocation + "-" + center;

                    } else {
                        distanceKey = center + "-" + currentLocation;
                    }

                    if(!currentLocation.equals(center)){
                        currentCost += calculateCost(0, DISTANCES.get(distanceKey));
                    }

                    currentCost += calculateCost(weight, DISTANCES.get(center + "-L1"));
                    currentLocation = "L1";
                }
            }
            minCost = Math.min(minCost, currentCost);
        }
        return minCost;
    }

    public static <T> List<List<T>> generatePermutations(List<T> list) {
        List<List<T>> permutations = new ArrayList<>();
        generatePermutationsHelper(list, new ArrayList<>(), permutations);
        return permutations;
    }

    private static <T> void generatePermutationsHelper(List<T> list, List<T> current, List<List<T>> permutations) {
        if (list.isEmpty()) {
            permutations.add(new ArrayList<>(current));
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            T element = list.get(i);
            List<T> remaining = new ArrayList<>(list);
            remaining.remove(i);
            current.add(element);
            generatePermutationsHelper(remaining, current, permutations);
            current.remove(current.size() - 1);
        }
    }

    public static void main(String[] args) {
        Map<String, Integer> order1 = Map.of("A", 1, "G", 1, "H", 1, "I", 3);
        Map<String, Integer> order2 = Map.of("A", 1, "B", 1, "C", 1, "G", 1, "H", 1, "I", 1);
        Map<String, Integer> order3 = Map.of("A", 1, "B", 1, "C", 1);
        Map<String, Integer> order4 = Map.of("A", 1, "B", 1, "C", 1, "D", 1);

        System.out.println("Order 1 cost: " + findMinimumCost(order1));
        System.out.println("Order 2 cost: " + findMinimumCost(order2));
        System.out.println("Order 3 cost: " + findMinimumCost(order3));
        System.out.println("Order 4 cost: " + findMinimumCost(order4));
    }
}