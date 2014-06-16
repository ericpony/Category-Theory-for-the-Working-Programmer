package example.five;

import static java.util.Objects.requireNonNull;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public class Monoid<T> implements BinaryOperator<T> {
  public static <T> Monoid<T> monoid(T zero, BinaryOperator<T> op) {
    return new Monoid<>(zero, op);
  }

  private final T zero;
  private final BiFunction<T, T, T> f;

  Monoid(T zero, BiFunction<T, T, T> f) {
    this.zero = requireNonNull(zero);
    this.f = requireNonNull(f);
  }

  @Override public T apply(T t, T u) {
    return f.apply(t, u);
  }

  /**
   * A value which when fed into either side of the
   * {@link #apply(Object, Object)} method, effectively creates an identity
   * function. Called `zero` as it is the same as adding zero to a number.
   */
  public final T zero() {
    return zero;
  }

  public static <T> void laws(Monoid<T> m, List<T> ts) {
    if (ts.size() >= 3) {
      T t1 = ts.head();
      T t2 = ts.tail().head();
      T t3 = ts.tail().tail().head();
      T tl = m.apply(t1, m.apply(t2, t3));
      T tr = m.apply(m.apply(t1, t2), t3);
      if (!tl.equals(tr))
        throw new AssertionError("m.apply fails associativity law: " + tl + " != " + tr);
    }
    for (T t : ts) {
      if (!m.apply(m.zero(), t).equals(t))
        throw new AssertionError("m.zero fails identity law (right) for: " + t);
      if (!m.apply(t, m.zero()).equals(t))
        throw new AssertionError("m.zero fails identity law (left) for: " + t);
    }
  }
}
