package example.five;

import static example.five.Monoid.monoid;
import static example.five.Unit.tap;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;
import static java.util.function.Function.identity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class Monoids {
  public static class Integers {
    public static Monoid<Integer> Add = monoid(0, (t, u) -> t + u);
    public static Monoid<Integer> Mul = monoid(1, (t, u) -> t * u);
    public static Monoid<Integer> Max = monoid(Integer.MIN_VALUE, (t, u) -> (t > u) ? t : u);
    public static Monoid<Integer> Min = monoid(Integer.MAX_VALUE, (t, u) -> (t > u) ? u : t);
    public static Monoid<Integer> GCD = monoid(0, Integers::gcd);
    public static Monoid<Integer> LCM = monoid(1, Integers::lcm);

    static int gcd(int t, int u) {
      if (u == 0)
        return t;
      return gcd(u, t % u); // thx, euclid
    }

    static int lcm(int a, int b) {
      return a * (b / gcd(a, b));
    }

    public static Monoid<Integer> constant(int i) {
      return Monoid.monoid(i, (t, u) -> i);
    }
  }

  public static class Booleans {
    public static Monoid<Boolean> Conjunction = monoid(true, (t, u) -> t && u);
    public static Monoid<Boolean> Disjunction = monoid(false, (t, u) -> t || u);
  }

  public static class Lists {
    public static <T> Monoid<List<T>> free() {
      return monoid(List.empty(), (t, u) -> u.prependAll(t));
    }
  }

  public static class Functions {
    public static <T> Monoid<Function<T, T>> endo() {
      return monoid(identity(), (t, u) -> t.andThen(u));
    }

    public static <A, M> Monoid<Function<A, M>> pointwise(Monoid<M> m) {
      return monoid(a -> m.zero(), (f, g) -> a -> m.apply(f.apply(a), g.apply(a)));
    }
  }

  public static class Sets {
    @SafeVarargs public static <T> Set<T> of(T... ts) {
      return unmodifiableSet(new HashSet<>(asList(ts)));
    }

    public static <T> Monoid<Set<T>> union() {
      return monoid(Collections.emptySet(), (t, u) -> {
        return unmodifiableSet(tap(new HashSet<>(t), s -> s.addAll(u)));
      });
    }
  }

  public static class Pairs {
    public static <A, B> Monoid<Pair<A, B>> pair(Monoid<A> ma, Monoid<B> mb) {
      return monoid(Pair.of(ma.zero(), mb.zero()), (t, u) -> Pair.of(ma.apply(t.left(), u.left()), mb.apply(t.right(), u.right())));
    }
  }
}
