package com.guillaumecl.puzzle8;

import java.util.Arrays;

public final class OutputLetter {
    private final int bitset;

    private OutputLetter(int bitset) {
        this.bitset = bitset;
    }

    public int getNumSet() {
        return Integer.bitCount(bitset);
    }

    public static OutputLetter fromString(String str) {
        boolean[] signals = new boolean[7];
        Arrays.fill(signals, false);
        str.chars()
                .map(chr -> chr - 'a')
                .forEach(idx -> signals[idx] = true);
        int num = 0;
        for (int idx = 0; idx < 7; idx++) {
            num = (num << 1) | (signals[idx] ? 0 : 1);
        }
        return new OutputLetter(num);
    }
}
