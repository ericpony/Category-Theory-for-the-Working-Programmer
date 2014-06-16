package example.five;

import static java.util.Objects.requireNonNull;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public final class Pair<A, B> {
  public static <A, B> Pair<A, B> of(A a, B b) {
    return new Pair<>(a, b);
  }
  
  public static <A, B> A first(Pair<A, B> p) {
    return p.left();
  }

  public static <A, B> B second(Pair<A, B> p) {
    return p.right();
  }

  private final A a;
  private final B b;
  
  Pair(A a, B b) {
    this.a = requireNonNull(a);
    this.b = requireNonNull(b);
  }

  public A left() {
    return a;
  }

  public B right() {
    return b;
  }

  public boolean both(BiPredicate<A, B> p) {
    return p.test(a, b);
  }
  
  public <T> T fold(BiFunction<A, B, T> f) {
    return f.apply(a, b);
  }

  @Override public int hashCode() {
    final int prime = 31;
    return prime * ((prime * a.hashCode()) + b.hashCode());
  }

  @Override public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || (getClass() != obj.getClass()))
      return false;
    Pair<?, ?> other = (Pair<?, ?>) obj;
    return a.equals(other.a) && b.equals(other.b);
  }

  @Override public String toString() {
    return "Pair(" + a + ", " + b + ")";
  }
}
