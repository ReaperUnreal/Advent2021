package com.guillaumecl.puzzle10;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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

    private boolean isOpenBrace(int c) {
        return (c == '(') || (c == '[') || (c == '{') || (c == '<');
    }

    private OptionalInt matchBrace(Stack<Integer> chars, int openBrace, int closeBrace, boolean showErrors) {
        int c0 = chars.pop();
        if (c0 != openBrace) {
            System.err.println("Wrong brace: " + c0);
            return OptionalInt.empty();
        }

        if (chars.empty()) {
            return OptionalInt.empty();
        }
        int c = chars.peek();
        while (isOpenBrace(c)) {
            var innerMatch = OptionalInt.empty();
            if (c == '(') {
                innerMatch = matchBrace(chars, '(', ')', showErrors);
            } else if (c == '[') {
                innerMatch = matchBrace(chars, '[', ']', showErrors);
            } else if (c == '{') {
                innerMatch = matchBrace(chars, '{', '}', showErrors);
            } else if (c == '<') {
                innerMatch = matchBrace(chars, '<', '>', showErrors);
            } else {
                System.err.println("Unknown brace: " + c);
                return OptionalInt.empty();
            }

            // check to see if corruption found
            if (innerMatch.isPresent()) {
                return innerMatch;
            }

            if (chars.empty()) {
                return OptionalInt.empty();
            }
            c = chars.peek();
        }

        int c1 = chars.pop();
        if (c1 == closeBrace) {
            return OptionalInt.empty();
        } else {
            if (showErrors) {
                System.out.println("Expected " + Character.toString(closeBrace) + " but found " + Character.toString(c1));
            }
            return OptionalInt.of(c1);
        }
    }

    private OptionalInt getLineCorruption(String line) {
        return getLineCorruption(line, true);
    }

    private OptionalInt getLineCorruption(String line, boolean showErrors) {
        var chars = line.chars().mapToObj(x -> x).collect(Collectors.toList());
        Stack<Integer> s = new Stack<>();
        for (int idx = chars.size() - 1; idx >= 0; idx--) {
            s.push(chars.get(idx));
        }
        int c = s.peek();
        if (c == '(') {
            return matchBrace(s, '(', ')', showErrors);
        } else if (c == '[') {
            return matchBrace(s, '[', ']', showErrors);
        } else if (c == '{') {
            return matchBrace(s, '{', '}', showErrors);
        } else if (c == '<') {
            return matchBrace(s, '<', '>', showErrors);
        } else {
            System.err.println("Unknown brace: " + c);
            return OptionalInt.empty();
        }
    }

    private int getCorruptionScore(int chr) {
        if (chr == ')') {
            return 3;
        } else if (chr == ']') {
            return 57;
        } else if (chr == '}') {
            return 1197;
        } else if (chr == '>') {
            return 25137;
        } else {
            System.err.println("Unknown character to score: " + chr);
            return -1;
        }
    }

    private void dumpAutocomplete(Stack<Integer> orig) {
        Stack<Integer> s = new Stack<>();
        s.addAll(orig);
        String str = "";
        while (!s.empty()) {
            str = Character.toString(s.pop()) + str;
        }
        System.out.println(str);
    }

    private void matchAuto(Stack<Integer> chars, int openBrace, int closeBrace, Stack<Integer> output) {
        int c0 = chars.pop();
        if (c0 != openBrace) {
            System.err.println("Wrong brace: " + c0);
            return;
        }

        if (chars.empty()) {
            output.push(closeBrace);
            return;
        }
        int c = chars.peek();
        while (isOpenBrace(c)) {
            if (c == '(') {
                matchAuto(chars, '(', ')', output);
            } else if (c == '[') {
                matchAuto(chars, '[', ']', output);
            } else if (c == '{') {
                matchAuto(chars, '{', '}', output);
            } else if (c == '<') {
                matchAuto(chars, '<', '>', output);
            } else {
                System.err.println("Unknown brace: " + c);
                return;
            }

            if (chars.empty()) {
                output.push(closeBrace);
                return;
            }
            c = chars.peek();
        }

        if (chars.empty()) {
            output.push(closeBrace);
            return;
        }
        int c1 = chars.pop();
        if (c1 == closeBrace) {
            return;
        } else {
            System.out.println("Expected " + Character.toString(closeBrace) + " but found " + Character.toString(c1));
            return;
        }
    }

    private Stack<Integer> getLineAutocomplete(String line) {
        var chars = line.chars().mapToObj(x -> x).collect(Collectors.toList());
        Stack<Integer> s = new Stack<>();
        for (int idx = chars.size() - 1; idx >= 0; idx--) {
            s.push(chars.get(idx));
        }
        Stack<Integer> output = new Stack<>();
        while (!s.empty()) {
            int c = s.peek();
            if (c == '(') {
                matchAuto(s, '(', ')', output);
            } else if (c == '[') {
                matchAuto(s, '[', ']', output);
            } else if (c == '{') {
                matchAuto(s, '{', '}', output);
            } else if (c == '<') {
                matchAuto(s, '<', '>', output);
            } else {
                System.err.println("Unknown brace: " + c);
                return new Stack<>();
            }
        }

        System.out.println(line);
        dumpAutocomplete(output);

        return output;
    }

    private long getAutocompleteScore(int chr) {
        if (chr == ')') {
            return 1L;
        } else if (chr == ']') {
            return 2L;
        } else if (chr == '}') {
            return 3L;
        } else if (chr == '>') {
            return 4L;
        } else {
            System.err.println("Unknown character to score: " + chr);
            return -1L;
        }
    }

    private long getAutocompleteScore(Stack<Integer> s) {
        List<Integer> l = new ArrayList<>(s.size());
        l.addAll(s);
        long total = 0L;
        for (int c : l) {
            long score = getAutocompleteScore(c);
            total = (total * 5L) + score;
        }
        System.out.println("Score: " + total);
        return total;
    }

    void part1(List<String> lines) {
        int totalScore = lines.stream()
                .map(this::getLineCorruption)
                .flatMapToInt(OptionalInt::stream)
                .map(this::getCorruptionScore)
                .sum();
        System.out.println("Total score: " + totalScore);
    }

    void part2(List<String> lines) {
        long[] arr = lines.stream()
                .filter(line -> this.getLineCorruption(line, false).isEmpty())
                .map(this::getLineAutocomplete)
                .mapToLong(this::getAutocompleteScore)
                .sorted()
                .toArray();
        long mid = arr[arr.length / 2];
        System.out.println("Mid Score: " + mid);
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
            List<String> lines = main.getLines("[({(<(())[]>[[{[]{<()<>>\n" +
                    "[(()[<>])]({[<{<<[]>>(\n" +
                    "{([(<{}[<>[]}>{[]{[(<()>\n" +
                    "(((({<>}<{<{<>}{[]{[]{}\n" +
                    "[[<[([]))<([[{}[[()]]]\n" +
                    "[{[{({}]{}}([{[{{{}}([]\n" +
                    "{<[[]]>}<{[{[{[]{()[[[]\n" +
                    "[<(<(<(<{}))><([]([]()\n" +
                    "<{([([[(<>()){}]>(<<{{\n" +
                    "<{([{{}}[<[[[<>{}]]]>[]]");
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
