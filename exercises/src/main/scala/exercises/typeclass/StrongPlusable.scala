package exercises.typeclass

trait StrongPlusable[A] extends Plusable[A]

object StrongPlusable {
  implicit def apply[A](implicit ev: StrongPlusable[A]): StrongPlusable[A] = ev

  implicit val int: StrongPlusable[Int] = new StrongPlusable[Int] {
    def plus(a1: Int, a2: Int): Int = a1 + a2
    def zero: Int = 0
  }
}