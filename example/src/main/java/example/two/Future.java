package example.two;

class Future<T> {
  T result;

  Future(T result) {
    this.result = result;
  }

  T get() {
    return result;
  }
}
