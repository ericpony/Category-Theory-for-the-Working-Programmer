package example.five;

import static java.util.Objects.requireNonNull;

public class Pair<A, B> {
  public static <A, B> Pair<A, B> of(A a, B b) {
    return new Pair<>(a, b);
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

  @Override public int hashCode() {
    final int prime = 31;
    return (prime * (prime * a.hashCode()) + b.hashCode());
  }

  @Override public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Pair<?, ?> other = (Pair<?, ?>) obj;
    return a.equals(other.a) && b.equals(other.b);
  }

  @Override public String toString() {
    return "Pair(" + a + ", " + b + ")";
  }
}
