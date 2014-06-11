package example.five;

import static example.five.Monoid.monoid;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public interface Foldable<T> {
  default <M> M foldMap(Function<T, M> f, Monoid<M> monoid) {
    return foldRight(monoid.zero(), (t, m) -> monoid.apply(f.apply(t), m)); 
  }

  default <A> A foldLeft(A zero, BiFunction<A, T, A> f) {
    return foldRight(zero, (t, a) -> f.apply(a, t));
  }

  default <A> A foldRight(A zero, BiFunction<T, A, A> f) {
    return foldLeft(zero, (t, a) -> f.apply(a, t));
  }

  default T fold(T zero, BinaryOperator<T> f) {
    return foldMap(Function.identity(), monoid(zero, f));
  }

  default List<T> toList() {
    return foldRight(List.empty(), (t, ts) -> ts.prepend(t));
  }
}
