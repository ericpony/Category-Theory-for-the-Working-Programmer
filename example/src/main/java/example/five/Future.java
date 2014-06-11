package example.five;

import java.util.function.Function;

class Future<T> {
  T result;

  Future(T result) {
    this.result = result;
  }

  T get() {
    return result;
  }

  static <T> Future<T> unit(T t) {
    return new Future<T>(t);
  }

  <R> Future<R> flatMap(Function<T, Future<R>> f) {
    return f.apply(result);
  }

  <R> Future<R> map(Function<T, R> f) {
    return flatMap(f.andThen(r -> unit(r)));
  }
}
