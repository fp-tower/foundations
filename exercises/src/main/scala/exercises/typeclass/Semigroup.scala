package exercises.typeclass

trait Semigroup[A] {
  def combine(x: A, y: A): A
}

object Semigroup {
  def apply[A](implicit ev: Semigroup[A]): Semigroup[A] = ev

  object syntax {
    implicit class SemigroupOps[A](self: A) {
      def combine(other: A)(implicit ev: Semigroup[A]): A = ev.combine(self, other)
      def |+|(other: A)(implicit ev: Semigroup[A]): A     = combine(other)
    }
  }
}
