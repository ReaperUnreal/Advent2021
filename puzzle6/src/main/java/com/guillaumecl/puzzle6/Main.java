package com.guillaumecl.puzzle6;

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

    void part1() throws IOException {
        //List<String> lines = splitLines("3,4,3,1,2");
        List<String> lines = readLines();
        String input = lines.get(0);
        var p = Pool.fromString(input);
        System.out.println(p.toString());
        for (int idx = 0; idx < 80; idx++) {
            p.advance();
            System.out.println(idx +  "," + p.getSize());
        }

        System.out.println("Final Size: " + p.getSize());
    }

    void part2() throws IOException {
        //List<String> lines = splitLines("3,4,3,1,2");
        List<String> lines = readLines();
        String input = lines.get(0);
        var p = Pool.fromString(input);
        System.out.println(p.toString());
        for (int idx = 0; idx < 256; idx++) {
            p.advance();
            System.out.println(idx +  "," + p.getSize());
        }

        System.out.println("Final Size: " + p.getSize());
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
