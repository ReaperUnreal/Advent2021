package com.guillaumecl.puzzle3;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jooq.lambda.function.Function2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
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

    private long mostCommonAtPosition(List<String> list, int idx) {
        int oneCount = list.stream()
                .map(str -> str.charAt(idx))
                .mapToInt(chr -> chr.equals('0') ? 0 : 1)
                .sum();
        int zeroCount = list.size() - oneCount;
        return zeroCount > oneCount ? 0 : 1;
    }

    private long reverseToLeastCommon(long num, int len) {
        long mask = (1 << len) - 1;
        return (~num) & mask;
    }

    private long leastCommonAtPosition(List<String> list, int idx) {
        int oneCount = list.stream()
                .map(str -> str.charAt(idx))
                .mapToInt(chr -> chr.equals('0') ? 0 : 1)
                .sum();
        int zeroCount = list.size() - oneCount;
        return zeroCount > oneCount ? 1 : 0;
    }

    void part1() throws IOException {
        /*
        List<String> lines = splitLines("00100\n" +
                "11110\n" +
                "10110\n" +
                "10111\n" +
                "10101\n" +
                "01111\n" +
                "00111\n" +
                "11100\n" +
                "10000\n" +
                "11001\n" +
                "00010\n" +
                "01010");
         */
        List<String> lines = readLines();
        int len = lines.get(0).length();
        long gammaRate = 0;
        for (int idx = 0; idx < len; idx++) {
            long mcn = mostCommonAtPosition(lines, idx);
            gammaRate = (gammaRate << 1) | (mcn & 0x1);
        }
        System.out.println("Gamma: " + Long.toBinaryString(gammaRate));
        long epsilonRate = reverseToLeastCommon(gammaRate, len);
        System.out.println("Epsilon: " + Long.toBinaryString(epsilonRate));
        long total = gammaRate * epsilonRate;
        System.out.println("Total: " + total);
    }

    private List<String> refineList(List<String> origList, char c, int idx) {
        return origList.stream()
                .filter(str -> str.charAt(idx) == c)
                .collect(Collectors.toList());
    }

    private long findRemainingNumber(List<String> origList, Function2<List<String>, Integer, Long> finder) {
        int idx = 0;
        List<String> list = List.copyOf(origList);
        while (list.size() > 1) {
            long numFound = finder.apply(list, idx);
            list = refineList(list, numFound == 0 ? '0' : '1', idx);
            System.out.println("Found " + numFound + " the refined to " + list.size() + " items");
            idx++;
        }
        return Long.parseUnsignedLong(list.get(0), 2);
    }

    void part2() throws IOException {
        /*
        List<String> lines = splitLines("00100\n" +
                "11110\n" +
                "10110\n" +
                "10111\n" +
                "10101\n" +
                "01111\n" +
                "00111\n" +
                "11100\n" +
                "10000\n" +
                "11001\n" +
                "00010\n" +
                "01010");
         */
        List<String> lines = readLines();
        long oxygenRating = findRemainingNumber(lines, this::mostCommonAtPosition);
        System.out.println("Oxygen: " + Long.toBinaryString(oxygenRating));
        long co2Rating = findRemainingNumber(lines, this::leastCommonAtPosition);
        System.out.println("CO2: " + Long.toBinaryString(co2Rating));
        long total = oxygenRating * co2Rating;
        System.out.println("Total: " + total);
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
