package hanbei.rangy;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;

public class RangeParser {

    List<Range> parse(String rangeHeader) {
        return Optional.ofNullable(rangeHeader)
                .map(s -> s.split("="))
                .filter(s -> s.length > 1)
                .map(s -> s[1])
                .map(s -> Arrays.stream(s.split(",")))
                .orElse(Stream.empty())
                .map(s -> s.split("-"))
                .filter(s -> s.length > 1)
                .map(s -> Range.of(parseInt(s[0].trim()), parseInt(s[1].trim())))
                .collect(toList());
    }

}
