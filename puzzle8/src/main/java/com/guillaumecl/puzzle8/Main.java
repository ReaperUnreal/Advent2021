package com.guillaumecl.puzzle8;

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

    void part1(List<String> lines) {
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

    private int convertToNumber(String str) {
        boolean[] indices = new boolean[7];
        Arrays.fill(indices, false);
        for (char c : str.toCharArray()) {
            indices[c - 'a'] = true;
        }
        int num = 0;
        for (int idx = 6; idx >= 0; idx--) {
            num = (num << 1) | (indices[idx] ? 0x1 : 0x0);
        }
        return num;
    }

    private String findStringOfLength(String[] arr, int len) {
        for (String str : arr) {
            if (str.length() == len) {
                return str;
            }
        }
        return null;
    }

    private int[] decodeInput(String input) {
        String[] parts = StringUtils.split(input,' ');
        int[] decoder = new int[10];
        decoder[8] = 127;
        decoder[1] = convertToNumber(findStringOfLength(parts, 2));
        decoder[7] = convertToNumber(findStringOfLength(parts, 3));
        decoder[4] = convertToNumber(findStringOfLength(parts, 4));
        int lDiff = decoder[4] & ~decoder[1];
        for (String part : parts) {
            int len = part.length();
            if (len == 2 || len == 3 || len == 4 || len == 7) { // 1 4 7 8
                continue;
            }
            int n = convertToNumber(part);
            if ((n & decoder[4]) == decoder[4]) { // 9
                decoder[9] = n;
            } else if ((n & decoder[1]) == decoder[1]) {
                if (len == 6) {
                    decoder[0] = n; // 0
                } else {
                    decoder[3] = n; // 3
                }
            } else if ((n & lDiff) == lDiff) {
                if (len == 5) {
                    decoder[5] = n; // 5
                } else {
                    decoder[6] = n; // 6
                }
            } else {
                decoder[2] = n; // 2
            }
        }
        return decoder;
    }

    private int getIndexOf(int[] arr, int num) {
        for (int idx = 0; idx < arr.length; idx++) {
            if (num == arr[idx]) {
                return idx;
            }
        }
        return -1;
    }

    private int[] decodeOutput(int[] decoder, String output) {
        String parts[] = StringUtils.split(output, ' ');
        int[] numbers = new int[4];
        for (int idx = 0; idx < 4; idx++) {
            int currNum = convertToNumber(parts[idx]);
            numbers[idx] = getIndexOf(decoder, currNum);
        }
        return numbers;
    }

    private int decodeLine(String line) {
        String parts[] = StringUtils.splitByWholeSeparator(line, " | ");
        int[] decoded = decodeInput(parts[0]);
        int[] numbers = decodeOutput(decoded, parts[1]);
        int num = 0;
        for (int currDig : numbers) {
            num = (num * 10) + currDig;
        }
        return num;
    }

    void part2(List<String> lines) {
        long total = lines.stream()
                .mapToInt(this::decodeLine)
                .sum();
        System.out.println("Total: " + total);
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
            List<String> lines = main.getLines("be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe\n" +
                    "  edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc\n" +
                    "  fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg\n" +
                    "  fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb\n" +
                    "  aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea\n" +
                    "  fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb\n" +
                    "  dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe\n" +
                    "  bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef\n" +
                    "  egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb\n" +
                    "  gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce");
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
