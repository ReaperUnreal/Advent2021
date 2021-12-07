package com.guillaumecl.puzzle5;

import java.util.Arrays;

public final class Board {
    private final int width;
    private final int height;
    private final int[] cells;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        cells = new int[width * height];
    }

    public void applyLine(Line line) {
        if (line.isHorizontal()) {
            int x1 = line.getStart().getX();
            int x2 = line.getEnd().getX();
            applyHorizontal(Math.min(x1, x2), Math.max(x1, x2), line.getStart().getY());
        } else if (line.isVertical()) {
            int y1 = line.getStart().getY();
            int y2 = line.getEnd().getY();
            applyVertical(line.getStart().getX(), Math.min(y1, y2), Math.max(y1, y2));
        } else {
            int x1 = line.getStart().getX();
            int x2 = line.getEnd().getX();
            int y1 = line.getStart().getY();
            int y2 = line.getEnd().getY();
            if (x1 < x2) {
                // already left->right
                if (y1 < y2) {
                    applyDiagonalDown(x1, x2, y1);
                } else {
                    applyDiagonalUp(x1, x2, y1);
                }
            } else {
                // right -> left
                if (y1 < y2) {
                    applyDiagonalUp(x2, x1, y2);
                } else {
                    applyDiagonalDown(x2, x1, y2);
                }
            }
        }
    }

    private void applyHorizontal(int x1, int x2, int y) {
        for (int x = x1; x <= x2; x++) {
            applyCoord(x, y);
        }
    }

    private void applyVertical(int x, int y1, int y2) {
        for (int y = y1; y <= y2; y++) {
            applyCoord(x, y);
        }
    }

    private void applyDiagonalDown(int x1, int x2, int y1) {
        for (int x = x1, y = y1; x <= x2; x++, y++) {
            applyCoord(x, y);
        }
    }

    private void applyDiagonalUp(int x1, int x2, int y1) {
        for (int x = x1, y = y1; x <= x2; x++, y--) {
            applyCoord(x, y);
        }
    }

    private void applyCoord(int x, int y) {
        cells[x + width * y]++;
    }

    public long getOverlapCount() {
        return Arrays.stream(cells)
                .filter(n -> n > 1)
                .count();
    }

    @Override
    public String toString() {
        String str = "";
        int idx = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++, idx++) {
                int num = cells[idx];
                if (num == 0) {
                    str += ".";
                } else {
                    str += Integer.toString(num);
                }
            }
            str += "\n";
        }
        return str;
    }
}
