package com.guillaumecl.puzzle5;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jooq.lambda.function.Function2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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

    private void dumpSegments(List<Line> segments) {
        System.out.println("All " + segments.size() + " segments:");
        segments.stream()
                .map(Line::toString)
                .forEach(System.out::println);
    }

    void part1() throws IOException {
        /*
        List<String> lines = splitLines("0,9 -> 5,9\n" +
                "8,0 -> 0,8\n" +
                "9,4 -> 3,4\n" +
                "2,2 -> 2,1\n" +
                "7,0 -> 7,4\n" +
                "6,4 -> 2,0\n" +
                "0,9 -> 2,9\n" +
                "3,4 -> 1,4\n" +
                "0,0 -> 8,8\n" +
                "5,5 -> 8,2");
         */
        List<String> lines = readLines();
        List<Line> segments = lines.stream()
                .map(Line::fromString)
                .filter(line -> line.isHorizontal() || line.isVertical())
                .collect(Collectors.toList());
        dumpSegments(segments);

        int maxX = segments.stream()
                .flatMap(s -> Stream.of(s.getStart(), s.getEnd()))
                .mapToInt(Coord::getX)
                .max()
                .orElse(-1);

        int maxY = segments.stream()
                .flatMap(s -> Stream.of(s.getStart(), s.getEnd()))
                .mapToInt(Coord::getY)
                .max()
                .orElse(-1);

        System.out.println("Board size: (" + (maxX + 1) + ", " + (maxY + 1) + ")");
        var b = new Board(maxX + 1, maxY + 1);
        int numSegments = segments.size();
        for (int idx = 0; idx < numSegments; idx++) {
            Line segment = segments.get(idx);
            System.out.println("Applying segment " + idx + " of " + numSegments + ": " + segment.toString());
            b.applyLine(segment);
        }

        System.out.println("Overlap count: " + b.getOverlapCount());
    }

    void part2() throws IOException {
        /*
        List<String> lines = splitLines("0,9 -> 5,9\n" +
                "8,0 -> 0,8\n" +
                "9,4 -> 3,4\n" +
                "2,2 -> 2,1\n" +
                "7,0 -> 7,4\n" +
                "6,4 -> 2,0\n" +
                "0,9 -> 2,9\n" +
                "3,4 -> 1,4\n" +
                "0,0 -> 8,8\n" +
                "5,5 -> 8,2");
         */
        List<String> lines = readLines();
        List<Line> segments = lines.stream()
                .map(Line::fromString)
                .collect(Collectors.toList());
        dumpSegments(segments);

        int maxX = segments.stream()
                .flatMap(s -> Stream.of(s.getStart(), s.getEnd()))
                .mapToInt(Coord::getX)
                .max()
                .orElse(-1);

        int maxY = segments.stream()
                .flatMap(s -> Stream.of(s.getStart(), s.getEnd()))
                .mapToInt(Coord::getY)
                .max()
                .orElse(-1);

        System.out.println("Board size: (" + (maxX + 1) + ", " + (maxY + 1) + ")");
        var b = new Board(maxX + 1, maxY + 1);
        int numSegments = segments.size();
        for (int idx = 0; idx < numSegments; idx++) {
            Line segment = segments.get(idx);
            System.out.println("Applying segment " + idx + " of " + numSegments + ": " + segment.toString());
            b.applyLine(segment);
        }

        System.out.println("Overlap count: " + b.getOverlapCount());
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
