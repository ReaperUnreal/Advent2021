package com.guillaumecl.puzzle5;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class Line {
    private final Coord start;
    private final Coord end;

    public Line(Coord start, Coord end) {
        this.start = start;
        this.end = end;
    }

    public Coord getStart() {
        return start;
    }

    public Coord getEnd() {
        return end;
    }

    public boolean isHorizontal() {
        return start.getY() == end.getY();
    }

    public boolean isVertical() {
        return start.getX() == end.getX();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Line line = (Line) o;

        return new EqualsBuilder()
                .append(start, line.start)
                .append(end, line.end)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(start)
                .append(end)
                .toHashCode();
    }

    @Override
    public String toString() {
        return start.toString() + " -> " + end.toString();
    }

    public static Line fromString(String str) {
        String[] parts = StringUtils.splitByWholeSeparator(str, " -> ");
        Coord start = Coord.fromString(parts[0]);
        Coord end = Coord.fromString(parts[1]);
        return new Line(start, end);
    }
}
