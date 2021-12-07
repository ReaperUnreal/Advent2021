package com.guillaumecl.puzzle5;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class Coord {
    private final int x;
    private final int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Coord coord = (Coord) o;

        return new EqualsBuilder()
                .append(x, coord.x)
                .append(y, coord.y)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(x)
                .append(y)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }

    public static Coord fromString(String str) {
        String[] parts = StringUtils.split(str, ',');
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        if (x < 0) {
            System.err.println("Negative X found: " + x);
        }
        if (y < 0) {
            System.err.println("Negative Y found: " + y);
        }
        return new Coord(x, y);
    }
}
