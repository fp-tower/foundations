package answers.dataprocessing

trait Monoid[A] extends Semigroup[A] {
  def default: A
}

object Monoid {
  val maxOption: Monoid[Option[Double]] = option(Semigroup.max)
  val minOption: Monoid[Option[Double]] = option(Semigroup.min)
  val sumDouble: Monoid[Double] = new Monoid[Double] {
    val default: Double                                = 0.0
    def combine(first: Double, second: Double): Double = first + second
  }
  val sumInt: Monoid[Int] = new Monoid[Int] {
    val default: Int                          = 0
    def combine(first: Int, second: Int): Int = first + second
  }

  def option[A](semigroup: Semigroup[A]): Monoid[Option[A]] =
    new Monoid[Option[A]] {
      val default: Option[A] = None

      def combine(optFirst: Option[A], optSecond: Option[A]): Option[A] =
        (optFirst, optSecond) match {
          case (Some(first), Some(second)) => Some(semigroup.combine(first, second))
          case (Some(first), None)         => Some(first)
          case (None, Some(second))        => Some(second)
          case (None, None)                => None
        }
    }
}
