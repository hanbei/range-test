package hanbei.rangy;

import java.util.Objects;

import static java.lang.Math.abs;

public class Range {

    private final int lower;
    private final int upper;

    private Range(int lower, int upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public static Range of(int lower, int upper) {
        return new Range(lower, upper);
    }

    public int lower() {
        return lower;
    }

    public int upper() {
        return upper;
    }

    public int length() {
        return abs(upper - lower) + 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Range range = (Range) o;
        return lower == range.lower &&
                upper == range.upper;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lower, upper);
    }

    @Override
    public String toString() {
        return "(" + lower + "-" + upper + ')';
    }
}
