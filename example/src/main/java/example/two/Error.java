package example.two;

class Error<T> {
  boolean ok;
  String message;
  T result;

  Error(String message) {
    this.ok = false;
    this.message = message;
  }

  Error(T result) {
    this.ok = true;
    this.result = result;
  }

  boolean ok() {
    return ok;
  }

  String message() {
    return message;
  }

  T result() {
    return result;
  }
}
