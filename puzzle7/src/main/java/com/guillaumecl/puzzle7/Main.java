package com.guillaumecl.puzzle7;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.lambda.function.Function2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    private List<String> readLines(String fileName) throws IOException {
        return Files.readAllLines(Paths.get(fileName))
                .stream()
                .map(StringUtils::trim)
                .filter(s -> !StringUtils.isBlank(s))
                .collect(Collectors.toList());
    }

    private List<String> readLines() throws IOException {
        return readLines("sources.txt");
    }

    private List<String> splitLines(String input) {
        return Arrays.stream(StringUtils.split(input, '\n'))
                .map(String::trim)
                .filter(s -> !StringUtils.isBlank(s))
                .collect(Collectors.toList());
    }

    private int calculateCostForPosition(List<Integer> positions, int pos) {
        int totalCost = 0;
        for (int cp : positions) {
            totalCost += Math.abs(pos - cp);
        }
        return totalCost;
    }

    private long calculateCostForPosition2(List<Integer> positions, long pos) {
        long totalCost = 0;
        for (int cp : positions) {
            long diff = Math.abs(pos - cp);
            totalCost += diff * (diff + 1) / 2;
        }
        return totalCost;
    }

    void part1() throws IOException {
        //List<String> lines = splitLines("16,1,2,0,4,2,7,1,2,14");
        List<String> lines = readLines();
        List<Integer> positions = Arrays.stream(StringUtils.split(lines.get(0), ','))
                .map(Integer::parseUnsignedInt)
                .collect(Collectors.toList());

        Collections.sort(positions);
        int median = positions.get(positions.size() / 2);
        int cost = calculateCostForPosition(positions, median);
        System.out.println("Median: " + positions.get(positions.size() / 2));
        System.out.println("Cost: " + cost);
    }

    private double calcGeomean(List<Integer> list) {
        return Math.sqrt(list.stream()
                .mapToInt(x -> x * x)
                .sum());
    }

    private Pair<Long, Long> findMin(List<Integer> positions, long middle, long range) {
        long start = middle - range;
        long end = middle + range;
        long minCost = calculateCostForPosition2(positions, start);
        long min = start;
        for (long pos = start + 1; pos <= end; pos++) {
            long cost = calculateCostForPosition2(positions, pos);
            if (cost < minCost) {
                minCost = cost;
                min = pos;
            }
        }
        return Pair.of(min, minCost);
    }

    void part2() throws IOException {
        //List<String> lines = splitLines("16,1,2,0,4,2,7,1,2,14");
        List<String> lines = readLines();
        List<Integer> positions = Arrays.stream(StringUtils.split(lines.get(0), ','))
                .map(Integer::parseUnsignedInt)
                .collect(Collectors.toList());

        Collections.sort(positions);
        int median = positions.get(positions.size() / 2);
        double geomean = calcGeomean(positions);
        double avg = positions.stream().mapToDouble(x -> x).average().orElse(-1.0);
        System.out.println("Median: " + median + " Cost: " + calculateCostForPosition2(positions, median));
        System.out.println("Geomean: " + geomean + " Cost: " + calculateCostForPosition2(positions, Math.round(geomean)));
        System.out.println("Average: " + avg + " Cost: " + calculateCostForPosition2(positions, Math.round(avg)));
        var pair = findMin(positions, Math.round(avg), 100);
        System.out.println("Min: " + pair.getLeft() + " Cost: " + pair.getRight());
    }

    public static void main(String[] args) {
        var main = new Main();
        try {
            main.part2();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
