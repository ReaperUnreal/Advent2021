package com.guillaumecl.puzzle4;

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

    private List<Integer> getCallNumbers(String strList) {
        return Arrays.stream(StringUtils.split(strList, ','))
                .map(Integer::parseUnsignedInt)
                .collect(Collectors.toList());
    }

    private List<BingoBoard> parseBingoBoards(List<String> lines) {
        int numBoards = (lines.size() - 1) / 5;
        List<BingoBoard> boards = new ArrayList<>();
        int idxLine = 1;
        for (int idxBoard = 0; idxBoard < numBoards; idxBoard++) {
            List<String> boardLines = new ArrayList<>();
            for (int idx = 0; idx < 5; idx++, idxLine++) {
                boardLines.add(lines.get(idxLine));
            }
            boards.add(BingoBoard.fromLines(boardLines));
        }
        return boards;
    }

    private void dumpBoards(List<BingoBoard> boards) {
        String str = boards.stream()
                .map(BingoBoard::toString)
                .collect(Collectors.joining("\n\n"));
        System.out.println("Boards:\n" + str);
    }

    private boolean doesWinningBoardExist(List<BingoBoard> boards) {
        return boards.stream().anyMatch(BingoBoard::isBoardWin);
    }

    private long getWinningBoardScore(List<BingoBoard> boards) {
        return boards.stream()
                .filter(BingoBoard::isBoardWin)
                .mapToLong(BingoBoard::getScore)
                .findFirst()
                .orElse(-1L);
    }

    private void applyCallNumber(List<BingoBoard> boards, int callNumber) {
        boards.forEach(board -> board.applyNumber(callNumber));
    }

    void part1() throws IOException {
        /*
        List<String> lines = splitLines("7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1\n" +
                "\n" +
                "22 13 17 11  0\n" +
                " 8  2 23  4 24\n" +
                "21  9 14 16  7\n" +
                " 6 10  3 18  5\n" +
                " 1 12 20 15 19\n" +
                "\n" +
                " 3 15  0  2 22\n" +
                " 9 18 13 17  5\n" +
                "19  8  7 25 23\n" +
                "20 11 10 24  4\n" +
                "14 21 16 12  6\n" +
                "\n" +
                "14 21 17 24  4\n" +
                "10 16 15  9 19\n" +
                "18  8 23 26 20\n" +
                "22 11 13  6  5\n" +
                " 2  0 12  3  7");
         */
        List<String> lines = readLines();
        var callNumbers = getCallNumbers(lines.get(0));
        var boards = parseBingoBoards(lines);
        dumpBoards(boards);

        long finalScore = -2;
        for (var callNumber : callNumbers) {
            System.out.println("Calling number " + callNumber);
            applyCallNumber(boards, callNumber);
            dumpBoards(boards);
            if (doesWinningBoardExist(boards)) {
                long boardScore = getWinningBoardScore(boards);
                System.out.println("Winning Board Score " + boardScore + " on final number " + callNumber);
                finalScore = boardScore * callNumber;
                break;
            }
        }
        System.out.println("Final Score: " + finalScore);
    }

    void part2() throws IOException {
        /*
        List<String> lines = splitLines("7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1\n" +
                "\n" +
                "22 13 17 11  0\n" +
                " 8  2 23  4 24\n" +
                "21  9 14 16  7\n" +
                " 6 10  3 18  5\n" +
                " 1 12 20 15 19\n" +
                "\n" +
                " 3 15  0  2 22\n" +
                " 9 18 13 17  5\n" +
                "19  8  7 25 23\n" +
                "20 11 10 24  4\n" +
                "14 21 16 12  6\n" +
                "\n" +
                "14 21 17 24  4\n" +
                "10 16 15  9 19\n" +
                "18  8 23 26 20\n" +
                "22 11 13  6  5\n" +
                " 2  0 12  3  7");
         */
        List<String> lines = readLines();
        var callNumbers = getCallNumbers(lines.get(0));
        var boards = parseBingoBoards(lines);
        dumpBoards(boards);
        Stack<Long> winningScores = new Stack<>();
        for (var callNumber : callNumbers) {
            System.out.println("Calling number " + callNumber);
            Stack<Integer> boardsToRemove = new Stack<>();
            applyCallNumber(boards, callNumber);

            // get winners
            for (int idx = 0; idx < boards.size(); idx++) {
                var board = boards.get(idx);
                if (board.isBoardWin()) {
                    long score = callNumber * board.getScore();
                    winningScores.push(score);
                    System.out.println("Winning board (score " + score + "):\n" + board.toString());
                    boardsToRemove.push(idx);
                }
            }

            // filter out winners
            while (!boardsToRemove.empty()) {
                int idxToRemove = boardsToRemove.pop();
                boards.remove(idxToRemove);
            }
            System.out.println("" + boards.size() + " boards remain");
            if (boards.isEmpty()) {
                break;
            }
        }

        // get last winner score
        System.out.println("Last winner score: " + winningScores.peek());
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
