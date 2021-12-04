package com.guillaumecl.puzzle4;

public final class BingoNumber {
    public int number;
    public boolean marked;

    public BingoNumber(int number) {
        this.number = number;
        marked = false;
    }

    @Override
    public String toString() {
        return String.format("%3d%s", number, marked ? 'X' : ' ');
    }
}
