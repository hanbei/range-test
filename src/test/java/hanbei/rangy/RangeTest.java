package hanbei.rangy;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RangeTest {

    @Test
    void null_range_is_empty() {
        List<Range> ranges = Range.parse(null);
        assertThat(ranges).isEmpty();
    }

    @Test
    void simple_range() {
        List<Range> ranges = Range.parse("bytes=0-99");

        assertThat(ranges).hasSize(1);
        assertThat(ranges.get(0)).isEqualTo(Range.of(0L, 99L));
    }

    @Test
    void multi_ranges() {
        List<Range> ranges = Range.parse("bytes=0-99, 100-200, 300-489");

        assertThat(ranges).hasSize(3);
        assertThat(ranges).containsExactly(Range.of(0L, 99L), Range.of(100L, 200L), Range.of(300L, 489L));
    }

    @Test
    void lower_to_end_range() {
        List<Range> ranges = Range.parse("bytes=123-");

        assertThat(ranges).hasSize(1);
        assertThat(ranges).contains(Range.lower(123L));
    }

    @Test
    void multi_lower_to_end_range() {
        List<Range> ranges = Range.parse("bytes=0-20, 123-");

        assertThat(ranges).hasSize(2);
        assertThat(ranges).contains(Range.of(0L, 20L), Range.lower(123L));
    }


    @Test
    void to_end_range() {
        List<Range> ranges = Range.parse("bytes=-1235");

        assertThat(ranges).hasSize(1);
        assertThat(ranges).contains(Range.upper(1235L));
    }

    @Test
    void multi_to_end_range() {
        List<Range> ranges = Range.parse("bytes=123-456, -1235");

        assertThat(ranges).hasSize(2);
        assertThat(ranges).contains(Range.of(123L, 456L), Range.upper(1235L));
    }

    @Test
    void malformed_range_multi_range() {
        List<Range> ranges = Range.parse("bytes=0-99, 101");

        assertThat(ranges).hasSize(1);
        assertThat(ranges).contains(Range.of(0L, 99L));
    }

    @Test
    void with_length_proper_range() {
        Range range = Range.of(9500L, 9999L).withLength(10000);

        assertThat(range).isEqualTo(Range.of(9500L, 9999L));
    }

    @Test
    void with_length_lower_range() {
        Range range = Range.lower(9500).withLength(10000);

        assertThat(range).isEqualTo(Range.of(9500L, 9999L));
    }

    @Test
    void with_length_upper_range() {
        Range range = Range.upper(500).withLength(10000);

        assertThat(range).isEqualTo(Range.of(9500L, 9999L));
    }
}