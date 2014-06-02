package object example {
  implicit def ScalaFunctionToJava[A, B](f: (A => B)) =
    new java.util.function.Function[A, B] {
      def apply(a: A) = f(a)
    }
}