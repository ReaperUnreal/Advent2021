package com.guillaumecl.puzzle1;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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

    private int countIncreases(List<Integer> list) {
        int prev = list.get(0);
        int count = 0;
        for (int idx = 1; idx < list.size(); idx++) {
            int curr = list.get(idx);
            if (curr > prev) {
                count++;
            }
            prev = curr;
        }
        return count;
    }

    void part1() throws IOException {
        /*
        List<String> lines = splitLines("199\n" +
                "200\n" +
                "208\n" +
                "210\n" +
                "200\n" +
                "207\n" +
                "240\n" +
                "269\n" +
                "260\n" +
                "263");
         */
        List<String> lines = readLines();
        List<Integer> depths = lines.stream().map(Integer::parseInt).collect(Collectors.toList());
        int count = countIncreases(depths);
        System.out.println(count);
    }

    void part2() throws IOException {
        /*
        List<String> lines = splitLines("199\n" +
                "200\n" +
                "208\n" +
                "210\n" +
                "200\n" +
                "207\n" +
                "240\n" +
                "269\n" +
                "260\n" +
                "263");
         */
        List<String> lines = readLines();
        List<Integer> depths = lines.stream().map(Integer::parseInt).collect(Collectors.toList());
        List<Integer> sums = new ArrayList<>();
        int sum = depths.get(0) + depths.get(1) + depths.get(2);
        sums.add(sum);
        for (int idx = 3; idx < depths.size(); idx++) {
            sum = sum - depths.get(idx - 3) + depths.get(idx);
            sums.add(sum);
        }
        int count = countIncreases(sums);
        System.out.println(count);
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
