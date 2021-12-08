package com.guillaumecl.puzzle8;

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

    private List<String> getLines(String defaultInput) throws IOException {
        if (useSource) {
            return readLines();
        } else {
            return splitLines(defaultInput);
        }
    }

    void part1() throws IOException {
        List<String> lines = getLines("be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe\n" +
                "  edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc\n" +
                "  fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg\n" +
                "  fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb\n" +
                "  aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea\n" +
                "  fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb\n" +
                "  dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe\n" +
                "  bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef\n" +
                "  egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb\n" +
                "  gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce");
        long numCount = lines.stream()
                .map(line -> StringUtils.splitByWholeSeparator(line, " | "))
                .map(parts -> parts[1])
                .map(output -> StringUtils.split(output, ' '))
                .flatMap(parts -> Arrays.stream(parts))
                .mapToInt(str -> str.length())
                .filter(num -> (num == 2) || (num == 4) || (num == 3) || (num == 7))
                .count();
        System.out.println("Counted " + numCount + " 1,4,7,8");
    }

    void part2() throws IOException {
    }

    public static void main(String[] args) {
        boolean useSource = args.length > 0 && StringUtils.equalsIgnoreCase(args[0], "-g");
        var main = new Main(useSource);
        try {
            main.part1();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
