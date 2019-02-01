package hanbei.rangy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RangeParserTest {

    private RangeParser parser;

    @BeforeEach
    void set_up() {
        parser = new RangeParser();
    }

    @Test
    void null_range_is_empty() {
        List<Range> ranges = parser.parse(null);
        assertThat(ranges).isEmpty();
    }

    @Test
    void simple_range() {
        List<Range> ranges = parser.parse("bytes=0-99");

        assertThat(ranges).hasSize(1);
        assertThat(ranges.get(0)).isEqualTo(Range.of(0, 99));
        assertThat(ranges.get(0).length()).isEqualTo(100);
    }

    @Test
    void multi_ranges() {
        List<Range> ranges = parser.parse("bytes=0-99, 100-200, 300-489");

        assertThat(ranges).hasSize(3);
        assertThat(ranges).containsExactly(Range.of(0, 99), Range.of(100, 200), Range.of(300, 489));
    }

    @Test
    void malformed_range() {
        List<Range> ranges = parser.parse("bytes=0-");

        assertThat(ranges).isEmpty();
    }

    @Test
    void malformed_range_multi_range() {
        List<Range> ranges = parser.parse("bytes=0-99, 101");

        assertThat(ranges).hasSize(1);
        assertThat(ranges).contains(Range.of(0, 99));
    }
}