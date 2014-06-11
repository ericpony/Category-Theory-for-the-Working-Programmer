package example.five;

public class Monoids {
  public static class Integers {
    public static Monoid<Integer> Add = Monoid.monoid(0, (t, u) -> t + u);
    public static Monoid<Integer> Mul = Monoid.monoid(1, (t, u) -> t * u);
    public static Monoid<Integer> Max = Monoid.monoid(Integer.MIN_VALUE, (t, u) -> (t > u) ? t : u);
    public static Monoid<Integer> Min = Monoid.monoid(Integer.MAX_VALUE, (t, u) -> (t > u) ? u : t);

    public static Monoid<Integer> constant(int i) {
      return Monoid.monoid(i, (t, u) -> i);
    }
  }

  public static class Booleans {
    public static Monoid<Boolean> Conjunction = Monoid.monoid(true, (t, u) -> t && u);
    public static Monoid<Boolean> Disjunction = Monoid.monoid(false, (t, u) -> t || u);
  }

  public static class Free {
    public static <T> Monoid<List<T>> free() {
      return Monoid.monoid(List.empty(), (t, u) -> t.isEmpty() ? u : u.isEmpty() ? t : u.prependAll(t));
    }
  }
}
