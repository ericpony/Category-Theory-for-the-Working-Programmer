package example.five;

import java.util.function.BinaryOperator;

public class Monoids {
  public static class Integers {
    public static Monoid<Integer> Addition = Monoid.monoid(0, new BinaryOperator<Integer>() {
      @Override public Integer apply(Integer t, Integer u) {
        return t + u;
      }
    });
    public static Monoid<Integer> Multiplication = Monoid.monoid(1, new BinaryOperator<Integer>() {
      @Override public Integer apply(Integer t, Integer u) {
        return t * u;
      }
    });
    public static Monoid<Integer> Max = Monoid.monoid(Integer.MIN_VALUE, new BinaryOperator<Integer>() {
      @Override public Integer apply(Integer t, Integer u) {
        return (t > u) ? t : u;
      }
    });
    public static Monoid<Integer> Min = Monoid.monoid(Integer.MAX_VALUE, new BinaryOperator<Integer>() {
      @Override public Integer apply(Integer t, Integer u) {
        return (t > u) ? u : t;
      }
    });

    public static Monoid<Integer> constant(int i) {
      return Monoid.monoid(i, new BinaryOperator<Integer>() {
        @Override public Integer apply(Integer t, Integer u) {
          return i;
        }
      });
    }
  }

  public static class Free {
    public static <T> Monoid<List<T>> free() {
      return Monoid.monoid(List.empty(), new BinaryOperator<List<T>>() {
        @Override public List<T> apply(List<T> t, List<T> u) {
          return t.isEmpty() ? u : u.isEmpty() ? t : u.prependAll(t);
        }
      });
    }
  }
}
