package com.guillaumecl.puzzle6;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class Pool {
    private final long[] spawnDays;

    private Pool(List<Integer> fishList) {
        spawnDays = new long[9];
        for (int idx = 0; idx < 9; idx++) {
            spawnDays[idx] = 0;
        }
        for (var fish : fishList) {
            spawnDays[fish]++;
        }
    }

    public void advance() {
        long numToSpawn = spawnDays[0];
        for (int idx = 0; idx < 8; idx++) {
            spawnDays[idx] = spawnDays[idx + 1];
        }
        spawnDays[6] += numToSpawn;
        spawnDays[8] = numToSpawn;
    }

    public long getSize() {
        return Arrays.stream(spawnDays).sum();
    }

    @Override
    public String toString() {
        return Arrays.stream(spawnDays).mapToObj(Long::toString).collect(Collectors.joining(","));
    }

    public static Pool fromString(String str) {
        var fishList = Arrays.stream(StringUtils.split(str, ','))
                .map(Integer::parseUnsignedInt)
                .collect(Collectors.toList());
        return new Pool(fishList);
    }
}
