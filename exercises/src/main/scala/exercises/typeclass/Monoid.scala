package exercises.typeclass

trait Monoid[A] {
  def plus(a1: A, a2: A): A
  def zero: A
}

object Monoid {
  def apply[A](implicit ev: Monoid[A]): Monoid[A] = ev

  object syntax extends MonoidSyntax

  implicit val int: Monoid[Int] = new Monoid[Int] {
    def plus(a1: Int, a2: Int): Int = a1 + a2
    def zero: Int = 0
  }

  implicit val long: Monoid[Long] = new Monoid[Long] {
    def plus(a1: Long, a2: Long): Long = a1 + a2
    def zero: Long = 0L
  }

  implicit val string: Monoid[String] = new Monoid[String] {
    def plus(a1: String, a2: String): String = a1 + a2
    def zero: String = ""
  }
}
