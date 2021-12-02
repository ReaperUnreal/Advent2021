package com.guillaumecl.puzzle2;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class Sub {
    private int x;
    private int d;
    private int aim;

    public Sub() {
        x = 0;
        d = 0;
        setAim(0);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public void updateX(int amount) {
        x += amount;
    }

    public void updateD(int amount) {
        d += amount;
    }

    public void updateAim(int amount) {
        aim += amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Sub sub = (Sub) o;

        return new EqualsBuilder()
                .append(x, sub.x)
                .append(d, sub.d)
                .append(getAim(), sub.getAim())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(x)
                .append(d)
                .append(getAim())
                .toHashCode();
    }

    @Override
    public String toString() {
        return "[" + x + ", " + d + "; aim = " + getAim() + "]";
    }

    public int getAim() {
        return aim;
    }

    public void setAim(int aim) {
        this.aim = aim;
    }

    public void down(int amount) {
        updateAim(amount);
    }

    public void up(int amount) {
        updateAim(-amount);
    }

    public void applyForward(int amount) {
        x += amount;
        d += (amount * aim);
    }
}
