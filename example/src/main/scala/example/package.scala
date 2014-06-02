trait LowPriorityConversions {
  implicit def ToJavaFunction[A, B](f: (A => B)) =
    new java.util.function.Function[A, B] {
      def apply(a: A) = f(a)
    }

  implicit def ToJavaBiFunction[A, B, C](f: ((A, B) => C)) =
    new java.util.function.BiFunction[A, B, C] {
      def apply(a: A, b: B) = f(a, b)
    }
}

package object example extends LowPriorityConversions {
  implicit def ToJavaConsumer[A](f: (A => Unit)) =
    new java.util.function.Consumer[A] {
      def accept(a: A) = f(a)
    }

  implicit def ToJavaBinaryOperator[A](f: ((A, A) => A)) =
    new java.util.function.BinaryOperator[A] {
      def apply(a: A, b: A) = f(a, b)
    }
}