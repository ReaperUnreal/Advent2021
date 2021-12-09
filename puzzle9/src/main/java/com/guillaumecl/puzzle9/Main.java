package com.guillaumecl.puzzle9;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.lambda.function.Function2;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    private final boolean useSource;

    public Main(boolean useSource) {
        this.useSource = useSource;
    }

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

    public List<String> getLines(String defaultInput) throws IOException {
        if (useSource) {
            return readLines();
        } else {
            return splitLines(defaultInput);
        }
    }

    private void dumpMap(int[] arr, int width) {
        int height = arr.length / width;
        for (int idx = 0, y = 0; y < height; y++) {
            String str = "";
            for (int x = 0; x < width; x++, idx++) {
                str += Integer.toString(arr[idx]);
            }
            System.out.println(str);
        }
    }

    private int getHeightAt(int[] map, int width, int x, int y) {
        return map[x + width * y];
    }

    private boolean isLowSpot(int x, int y, int[] map, int width) {
        int height = map.length / width;
        int h = getHeightAt(map, width, x, y);
        if (h == 9) { // highest value cannot be a low point
            return false;
        }
        if (x > 0 && getHeightAt(map, width, x - 1, y) <= h) {
            return false;
        }
        if (x < (width - 1) && getHeightAt(map, width, x + 1, y) <= h) {
            return false;
        }
        if (y > 0 && getHeightAt(map, width, x, y - 1) <= h) {
            return false;
        }
        if (y < (height - 1) && getHeightAt(map, width, x, y + 1) <= h) {
            return false;
        }
        return true;
    }

    private List<Integer> getLowSpotIndices(int[] map, int width) {
        List<Integer> lowSpots = new ArrayList<>();
        int height = map.length / width;
        for (int idx = 0, y = 0; y < height; y++) {
            for (int x = 0; x < width; x++, idx++) {
                if (isLowSpot(x, y, map, width)) {
                    lowSpots.add(idx);
                    System.out.println("Low spot at " + idx + " (" + x + ", " + y + "): " + map[idx]);
                }
            }
        }
        return lowSpots;
    }

    void part1(List<String> lines) {
        int width = lines.get(0).length();
        int[] heightMap = lines.stream()
                .flatMapToInt(String::chars)
                .map(x -> x - '0')
                .toArray();
        dumpMap(heightMap, width);

        int totalRisk = getLowSpotIndices(heightMap, width).stream()
                .mapToInt(x -> heightMap[x] + 1)
                .sum();
        System.out.println("Total risk: " + totalRisk);
    }

    private boolean isValidNeighbour(int[] map, int idx, Set<Integer> allBasinIndices, Set<Integer> currBasinIndices, int destH) {
        int h = map[idx];
        if (h == 9) {
            return false;
        }
        if (allBasinIndices.contains(idx)) {
            return false;
        }
        if (currBasinIndices.contains(idx)) {
            return false;
        }
        if (h >= destH) {
            return true;
        }
        return false;
    }

    private Set<Integer> getValidNeighbours(int[] map, int width, int idx, Set<Integer> allBasinIndices, Set<Integer> currBasinIndices) {
        Set<Integer> neighbours = new HashSet<>();
        int height = map.length / width;
        int x = idx % width;
        int y = idx / width;
        int h = map[idx];
        if (x > 0 && isValidNeighbour(map, idx - 1, allBasinIndices, currBasinIndices, h)) {
            neighbours.add(idx - 1);
        }
        if (x < (width - 1) && isValidNeighbour(map, idx + 1, allBasinIndices, currBasinIndices, h)) {
            neighbours.add(idx + 1);
        }
        if (y > 0 && isValidNeighbour(map, idx - width, allBasinIndices, currBasinIndices, h)) {
            neighbours.add(idx - width);
        }
        if (y < (height - 1) && isValidNeighbour(map, idx + width, allBasinIndices, currBasinIndices, h)) {
            neighbours.add(idx + width);
        }
        return neighbours;
    }

    private Set<Integer> growBasin(int[] map, int width, int lowIdx, Set<Integer> allBasinIndices) {
        if (allBasinIndices.contains(lowIdx)) {
            // already counted in another basin somehow
            return Collections.emptySet();
        }
        Set<Integer> currBasin = new HashSet<>();
        currBasin.add(lowIdx);
        Queue<Integer> edge = new LinkedList<>();
        edge.add(lowIdx);

        while (!edge.isEmpty()) {
            int idx = edge.poll();
            Set<Integer> validNeighbours = getValidNeighbours(map, width, idx, allBasinIndices, currBasin);
            currBasin.addAll(validNeighbours);
            for (int neighbourIdx : validNeighbours) {
                edge.offer(neighbourIdx);
            }
        }

        return currBasin;
    }

    void part2(List<String> lines) {
        int width = lines.get(0).length();
        int[] heightMap = lines.stream()
                .flatMapToInt(String::chars)
                .map(x -> x - '0')
                .toArray();
        dumpMap(heightMap, width);

        var lowSpots = getLowSpotIndices(heightMap, width);
        Set<Integer> allBasinIndices = new HashSet<>();
        List<Set<Integer>> basinList = new ArrayList<>(lowSpots.size());
        for (int lowSpotIdx : lowSpots) {
            var basin = growBasin(heightMap, width, lowSpotIdx, allBasinIndices);
            System.out.println("Basin " + lowSpotIdx + "(" + (lowSpotIdx % width) + ", " + (lowSpotIdx / width) + ") size: " + basin.size());
            basinList.add(basin);
        }

        int result = basinList.stream()
                .map(Set::size)
                .sorted((x, y) -> Integer.compare(y, x))
                .limit(3)
                .reduce(1, (x, y) -> x * y);
        System.out.println("Result: " + result);
    }

    public static void main(String[] args) {
        boolean useSource = false;
        boolean run2 = false;
        for (String arg : args) {
            if (StringUtils.equalsIgnoreCase(arg, "-g")) {
                useSource = true;
            } else if (StringUtils.equalsIgnoreCase(arg, "-2")) {
                run2 = true;
            }
        }
        var main = new Main(useSource);
        try {
            List<String> lines = main.getLines("2199943210\n" +
                    "3987894921\n" +
                    "9856789892\n" +
                    "8767896789\n" +
                    "9899965678");
            if (run2) {
                main.part2(lines);
            } else {
                main.part1(lines);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
