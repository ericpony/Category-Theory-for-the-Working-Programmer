package example.five;

import java.util.stream.Stream;

public final class Range {
  public static Stream<Integer> range(int from, int to) {
    return range(from, to, 1);
  }

  public static Stream<Integer> range(int from, int to, int step) {
    return Stream.concat(Stream.of(from), Stream.of(from).flatMap(t -> {
      if (t > to)
        return Stream.empty();
      else
        return Stream.of(t + step);
    }));
  }
}
