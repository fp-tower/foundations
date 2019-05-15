package exercises.typeclass

trait Monoid[A] extends Semigroup[A] {
  def empty: A
}

object Monoid {
  def apply[A](implicit ev: Monoid[A]): Monoid[A] = ev

  object syntax {
    def mempty[A](implicit ev: Monoid[A]): A = ev.empty
  }
}
