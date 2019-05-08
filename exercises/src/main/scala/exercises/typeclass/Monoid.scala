package exercises.typeclass

trait Monoid[A] {
  def combine(x: A, y: A): A
  def empty: A
}

object Monoid {
  def apply[A](implicit ev: Monoid[A]): Monoid[A] = ev

  object syntax {
    implicit class MonoidOps[A](self: A) {
      def combine(other: A)(implicit ev: Monoid[A]): A = ev.combine(self, other)
      def |+|(other: A)(implicit ev: Monoid[A]): A     = combine(other)
    }

    def mempty[A](implicit ev: Monoid[A]): A = ev.empty
  }
}
