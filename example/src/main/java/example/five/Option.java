package example.five;

import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class Option<T> implements Iterable<T>, Foldable<T> {
  public static <T> Option<T> unit(T t) {
    return new Some<>(requireNonNull(t));
  }

  public static <T> Option<T> none() {
    return new None<>();
  }

  /**
   * Catamorphism or interpreter for Option algebra.
   * @param s the supplier of the something when there is nothing
   * @param f the function that computes what to do with the value
   * @return the right thing
   */
  public abstract <R> R fold(Supplier<R> s, Function<T, R> f);

  //
  // implementations
  //

  static class Some<T> extends Option<T> {
    final T value;

    Some(T value) {
      this.value = value;
    }

    @Override public <R> R fold(Supplier<R> s, Function<T, R> f) {
      return f.apply(value);
    }
  }

  static class None<T> extends Option<T> {
    None() {}

    @Override public <R> R fold(Supplier<R> s, Function<T, R> f) {
      return s.get();
    }
  }

  //
  // usage
  //

  public final boolean isDefined() {
    return fold(() -> false, t -> true);
  }

  public final T getOrElse(T otherwise) {
    return fold(() -> otherwise, identity());
  }

  public final T get() throws NoSuchElementException {
    return fold(() -> {
      throw new NoSuchElementException();
    }, identity());
  }

  public final <R> Option<R> map(Function<T, R> f) {
    // cannot use unit as it checks for null
    return fold(() -> none(), t -> new Some<>(f.apply(t))); 
  }

  public final <R> Option<R> flatMap(Function<T, Option<R>> f) {
    return fold(() -> none(), t -> f.apply(t));
  }

  public final Option<T> filter(Predicate<T> p) {
    return fold(() -> none(), t -> p.test(t) ? this : none());
  }

  public final boolean exists(Predicate<T> p) {
    return fold(() -> false, t -> p.test(t));
  }

  @Override public final <A> A foldRight(A zero, BiFunction<T, A, A> f) {
    return fold(() -> zero, t -> f.apply(t, zero));
  }

  @Override public Iterator<T> iterator() {
    return fold(() -> Collections.emptyIterator(), t -> Collections.singleton(t).iterator());
  }

  @Override public String toString() {
    return fold(() -> "None()", t -> "Some(" + t + ")");
  }
}
