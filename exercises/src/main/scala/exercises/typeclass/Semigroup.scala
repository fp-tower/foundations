package exercises.typeclass

trait Semigroup[A] {
  def combine(a1: A, a2: A): A
}

object Semigroup {
  def apply[A](implicit ev: Semigroup[A]): Semigroup[A] = ev

  object syntax {
    implicit class SemigroupOps[A](self: A)(implicit ev: Semigroup[A]){
      def combine(other: A): A = ev.combine(self, other)
      def |+|(other: A): A = combine(other)
    }
  }
}