package example.five;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Singly-linked (cons) list. Prepends to start rather than adds to end (ie.
 * it's an immutable Stack).
 */
public abstract class List<T> implements Iterable<T>, Foldable<T> {
  @SafeVarargs public static <T> List<T> of(T... ts) {
    List<T> result = empty();
    for (int i = ts.length - 1; i >= 0; i--) {
      result = result.prepend(ts[i]);
    }
    return result;
  }

  public static <T> List<T> unit(T t) {
    return of(t);
  }

  public static <T> List<T> empty() {
    return new Empty<>();
  }

  public static final <T> List<T> concatenate(List<List<T>> ts) {
    //return ts.foldRight(empty(), (t, rlist) -> t.foldRight(rlist, (r, rs) -> rs.prepend(r)));
    return ts.flatMap(Function.identity());
  }

  // should not be extended outside of this compilation unit
  List() {}

  public final List<T> prepend(T t) {
    return new Node<>(this, t);
  }

  public final List<T> prependAll(List<T> ts) {
    return concatenate(of(ts, this));
  }

  public final <R> List<R> flatMap(Function<T, List<R>> f) {
    return foldRight(empty(), (t, rlist) -> f.apply(t).foldRight(rlist, (r, rs) -> rs.prepend(r)));
  }

  public final <R> List<R> map(Function<T, R> f) {
    return flatMap(f.andThen(r -> unit(r)));
    // return foldRight(empty(), (t, rs) -> rs.prepend(f.apply(t)));
  }

  public final <X> X foldLeft(X zero, BiFunction<X, T, X> f) {
    X result = zero;
    for (T t : this) {
      result = f.apply(result, t);
    }
    return result;
  }

  @Override public <M> M foldMap(Function<T, M> f, Monoid<M> monoid) {
    return foldLeft(monoid.zero(), (m, t) -> monoid.apply(m, f.apply(t)));
  }

  @Override public final T fold(T zero, BinaryOperator<T> f) {
    return foldLeft(zero, f);
  }

  public <X> X foldRight(X zero, BiFunction<T, X, X> f) {
    return reverse().foldLeft(zero, (x, t) -> f.apply(t, x));
  }

  public int size() {
    return foldLeft(0, (i, meh) -> i + 1);
  }

  //
  // abstract
  //

  public abstract boolean isEmpty();

  public abstract List<T> reverse();

  abstract T head() throws NoSuchElementException;

  abstract List<T> tail() throws NoSuchElementException;

  static class Node<T> extends List<T> {
    final List<T> next;
    final T value;

    Node(List<T> next, T value) {
      this.next = next;
      this.value = value;
    }

    public T head() {
      return value;
    }

    public List<T> tail() {
      return next;
    }

    public List<T> reverse() {
      return foldLeft(List.<T> empty(), (ts, t) -> ts.prepend(t));
    }

    @Override public boolean isEmpty() {
      return false;
    }
  }

  static class Empty<T> extends List<T> {
    @Override public List<T> reverse() {
      return this;
    }

    @Override public boolean isEmpty() {
      return true;
    }

    @Override public T head() {
      throw new NoSuchElementException();
    }

    @Override public List<T> tail() {
      throw new NoSuchElementException();
    }
  }

  public Iterator<T> iterator() {
    return new Iterator<T>() {
      List<T> current = List.this;

      @Override public boolean hasNext() {
        return !current.isEmpty();
      }

      @Override public T next() {
        try {
          return current.head();
        } finally {
          current = current.tail();
        }
      }
    };
  }

  @Override public String toString() {
    if (isEmpty())
      return "List()";
    final StringBuilder sb = new StringBuilder("List(");
    forEach(t -> sb.append(t).append(","));
    sb.setCharAt(sb.length() - 1, ')');
    return sb.toString();
  }
}
