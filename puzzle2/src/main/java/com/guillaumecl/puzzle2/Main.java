package com.guillaumecl.puzzle2;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

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

    private void applyLine(String line, Position p) {
        var parts = StringUtils.split(line, ' ');
        int amount = Integer.parseUnsignedInt(parts[1]);
        if (parts[0].equals("forward")) {
            // inc x
            p.updateX(amount);
        } else if (parts[0].equals("down")) {
            // inc y
            p.updateY(amount);
        } else if (parts[0].equals("up")) {
            // dec y
            p.updateY(-amount);
        }
    }

    private void applySubLine(String line, Sub s) {
        var parts = StringUtils.split(line, ' ');
        int amount = Integer.parseUnsignedInt(parts[1]);
        if (parts[0].equals("forward")) {
            s.applyForward(amount);
        } else if (parts[0].equals("down")) {
            s.down(amount);
        } else if (parts[0].equals("up")) {
            s.up(amount);
        }
    }

    void part1() throws IOException {
        /*
        List<String> lines = splitLines("forward 5\n" +
                "down 5\n" +
                "forward 8\n" +
                "up 3\n" +
                "down 8\n" +
                "forward 2");
         */
        List<String> lines = readLines();
        Position p = new Position(0, 0);
        System.out.println(p.toString());
        for (String line : lines) {
            applyLine(line, p);
            System.out.println(line + " " + p.toString());
        }
        System.out.println("Total: " + (p.getX() * p.getY()));
    }

    void part2() throws IOException {
        /*
        List<String> lines = splitLines("forward 5\n" +
                "down 5\n" +
                "forward 8\n" +
                "up 3\n" +
                "down 8\n" +
                "forward 2");
         */
        List<String> lines = readLines();
        Sub s = new Sub();
        System.out.println(s.toString());
        for (String line : lines) {
            applySubLine(line, s);
            System.out.println(line + " " + s.toString());
        }
        System.out.println("Total: " + (s.getX() * s.getD()));
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
