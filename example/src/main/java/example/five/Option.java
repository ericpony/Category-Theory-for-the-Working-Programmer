package example.five;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class Option<T> implements Iterable<T>, Foldable<T> {
  public static <T> Option<T> unit(T t) {
    return new Some<>(t);
  }

  public static <T> Option<T> none() {
    return new None<>();
  }

  public abstract <R> Option<R> map(Function<T, R> f);

  public abstract <R> Option<R> flatMap(Function<T, Option<R>> f);

  public abstract boolean isDefined();

  public abstract T get();

  public abstract T getOrElse(T otherwise);
  
  static class Some<T> extends Option<T> {
    final T value;

    Some(T value) {
      this.value = value;
    }

    @Override public <R> Option<R> map(Function<T, R> f) {
      return new Some<R>(f.apply(value));
    }

    @Override public <R> Option<R> flatMap(Function<T, Option<R>> f) {
      return f.apply(value);
    }

    @Override public boolean isDefined() {
      return true;
    }

    @Override public T get() {
      return value;
    }

    @Override public T getOrElse(T otherwise) {
      return value;
    }

    @Override public <A> A foldRight(A zero, BiFunction<T, A, A> f) {
      return f.apply(value, zero);
    }

    @Override public Iterator<T> iterator() {
      return Collections.singleton(value).iterator();
    }

    @Override public String toString() {
      return "Some(" + value + ")";
    }
  }

  static class None<T> extends Option<T> {
    None() {}

    @Override public <R> Option<R> map(Function<T, R> f) {
      return none();
    }

    @Override public <R> Option<R> flatMap(Function<T, Option<R>> f) {
      return new None<R>();
    }

    @Override public boolean isDefined() {
      return false;
    }

    @Override public T get() {
      throw new NoSuchElementException();
    }

    @Override public T getOrElse(T otherwise) {
      return otherwise;
    }

    @Override public Iterator<T> iterator() {
      return Collections.emptyIterator();
    }

    @Override public <A> A foldRight(A zero, BiFunction<T, A, A> f) {
      return zero;
    }


    @Override public String toString() {

      return "None()";
    }
  }
}
