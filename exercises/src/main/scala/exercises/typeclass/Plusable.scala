package exercises.typeclass

trait Plusable[A] {
  def plus(a1: A, a2: A): A
  def zero: A
}

object Plusable {
  def apply[A](implicit ev: Plusable[A]): Plusable[A] = ev

  object syntax extends PlusableSyntax

  implicit val int: Plusable[Int] = new Plusable[Int] {
    def plus(a1: Int, a2: Int): Int = a1 + a2
    def zero: Int = 0
  }

  implicit val long: Plusable[Long] = new Plusable[Long] {
    def plus(a1: Long, a2: Long): Long = a1 + a2
    def zero: Long = 0L
  }

  implicit val string: Plusable[String] = new Plusable[String] {
    def plus(a1: String, a2: String): String = a1 + a2
    def zero: String = ""
  }
}
