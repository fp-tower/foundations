package exercises.typeclass

trait StrongMonoid[A] extends Monoid[A]

object StrongMonoid {
  implicit def apply[A](implicit ev: StrongMonoid[A]): StrongMonoid[A] = ev

  implicit val int: StrongMonoid[Int] = new StrongMonoid[Int] {
    def combine(a1: Int, a2: Int): Int = a1 + a2
    def empty: Int                     = 0
  }
}
