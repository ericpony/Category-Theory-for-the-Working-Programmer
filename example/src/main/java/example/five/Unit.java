package example.five;

public enum Unit {
  Unit;

  interface Effect<T> {
    void apply(T t);

    default Unit perform(T t) {
      apply(t);
      return Unit;
    }
  }

  /** perform the effect on the input, then return it */
  public static <T> T tap(T t, Unit.Effect<T> effect) {
    effect.perform(t);
    return t;
  }
}
