package exercises.typeclass

trait Semigroup[A] {
  def combine(x: A, y: A): A
}

object Semigroup {
  def apply[A](implicit ev: Semigroup[A]): Semigroup[A] = ev

  object syntax {
    implicit class SemigroupOps[A](self: A) {
      // should be |+| but I had to use a different symbol because Monoid doesn't extends Semigroup
      def |++|(other: A)(implicit ev: Semigroup[A]): A = ev.combine(self, other)
    }
  }

  // Should be removed when we make Monoid extends Semigroup in TypeclassExercises
  implicit def fromMonoid[A: Monoid] = new Semigroup[A] {
    def combine(x: A, y: A): A = Monoid[A].combine(x, y)
  }
}
