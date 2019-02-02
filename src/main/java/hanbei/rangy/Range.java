package hanbei.rangy;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.google.common.base.Strings.emptyToNull;
import static java.lang.Long.parseLong;
import static java.util.stream.Collectors.toList;

public final class Range {

    private static final Pattern RANGE_PATTERN = Pattern.compile("\\d*-\\d*");
    private final Long lower;
    private final Long upper;

    private Range(Long lower, Long upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public static Range of(Long lower, Long upper) {
        return new Range(lower, upper);
    }

    public static Range lower(long lower) {
        return new Range(lower, null);
    }

    public static Range upper(long upper) {
        return new Range(null, upper);
    }

    public static List<Range> parse(String rangeHeader) {
        return Optional.ofNullable(rangeHeader)
                .map(s1 -> s1.split("="))
                .filter(s1 -> s1.length > 1)
                .map(s1 -> s1[1])
                .map(s1 -> Arrays.stream(s1.split(",")))
                .orElse(Stream.empty())
                .filter(RANGE_PATTERN.asPredicate())
                .map(s -> s.split("-"))
                .map(s -> of(toLong(s, 0), toLong(s, 1)))
                .collect(toList());
    }

    private static Long toLong(String[] string, int index) {
        if (string.length <= index) {
            return null;
        }
        String s = emptyToNull(string[index].trim());
        if (s != null) {
            return parseLong(s.trim());
        }
        return null;
    }

    public Range withLower(long lower) {
        return new Range(lower, upper);
    }

    public Range withUpper(long upper) {
        return new Range(lower, upper);
    }

    public Optional<Long> lower() {
        return Optional.ofNullable(lower);
    }

    public Optional<Long> upper() {
        return Optional.ofNullable(upper);
    }

    public Range withLength(long length) {
        long lower;
        long upper;
        if (lower().isPresent()) {
            lower = lower().get();
            if (upper().isPresent()) {
                upper = this.upper;
            } else {
                upper = length;
            }
        } else {
            if (upper().isPresent()) {
                upper = length;
                lower = length - this.upper;
            } else {
                lower = 0;
                upper = length;
            }
        }
        return Range.of(lower, upper);
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
        return Objects.equals(lower, range.lower) &&
                Objects.equals(upper, range.upper);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lower, upper);
    }

    @Override
    public String toString() {
        return "(" + lower + "-" + upper + ')';
    }

    public long length() {
        return upper - lower + 1;
    }
}
