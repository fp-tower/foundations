package exercises.typeclass

trait Monoid[A] {
  def combine(a1: A, a2: A): A
  def empty: A
}

object Monoid {
  def apply[A](implicit ev: Monoid[A]): Monoid[A] = ev

  object syntax {
    implicit class MonoidOps[A](self: A)(implicit ev: Monoid[A]){
      def combine(other: A): A = ev.combine(self, other)
      def |+|(other: A): A = combine(other)
    }

    def mempty[A](implicit ev: Monoid[A]): A = ev.empty
  }
}