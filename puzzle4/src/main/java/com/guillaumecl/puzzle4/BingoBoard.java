package com.guillaumecl.puzzle4;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class BingoBoard {
    private List<List<BingoNumber>> numbers;

    private BingoBoard(List<List<Integer>> rawNumbers) {
        numbers = rawNumbers.stream()
                .map(row -> {
                    return row.stream()
                            .map(BingoNumber::new)
                            .collect(Collectors.toList());
                }).collect(Collectors.toList());
    }

    public void applyNumber(int callNumber) {
        for (var row : numbers) {
            for (var number : row) {
                if (number.number == callNumber) {
                    number.marked = true;
                }
            }
        }
    }

    public boolean isBoardWin() {
        // check rows
        for (var row : numbers) {
            boolean allMarked = true;
            for (var number : row) {
                allMarked = allMarked & number.marked;
            }
            if (allMarked) {
                System.out.println("Found winning row");
                return true;
            }
        }

        // check columns
        for (int x = 0; x < 5; x++) {
            boolean allMarked = true;
            for (int y = 0; y < 5; y++) {
                var bn = numbers.get(y).get(x);
                allMarked = allMarked & bn.marked;
            }
            if (allMarked) {
                System.out.println("Found winning column");
                return true;
            }
        }
        return false;
    }

    public long getScore() {
        return numbers.stream()
                .flatMap(Collection::stream)
                .filter(bn -> !bn.marked)
                .mapToLong(bn -> bn.number)
                .sum();
    }

    private String rowToString(List<BingoNumber> row) {
        return row.stream().map(BingoNumber::toString).collect(Collectors.joining(" "));
    }

    @Override
    public String toString() {
        return numbers.stream().map(this::rowToString).collect(Collectors.joining("\n"));
    }

    public static BingoBoard fromLines(List<String> lines) {
        return new BingoBoard(lines.stream()
        .map(str -> {
            return Arrays.stream(StringUtils.split(str, ' '))
                    .filter(s -> !StringUtils.isBlank(s))
                    .map(Integer::parseUnsignedInt)
                    .collect(Collectors.toList());
        }).collect(Collectors.toList()));
    }
}
