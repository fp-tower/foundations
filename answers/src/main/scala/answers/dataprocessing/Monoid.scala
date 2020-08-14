package answers.dataprocessing

trait Monoid[A] {
  def isCommutative: Boolean

  def default: A
  def combine(first: A, second: A): A
}

object Monoid {
  val maxOption: Monoid[Option[Double]] = combineOption[Double](true, _ max _)
  val minOption: Monoid[Option[Double]] = combineOption[Double](true, _ min _)
  val sum: Monoid[Double] = new Monoid[Double] {
    val isCommutative: Boolean                         = true
    val default: Double                                = 0.0
    def combine(first: Double, second: Double): Double = first + second
  }

  def combineOption[A](_isCommutative: Boolean, _combine: (A, A) => A): Monoid[Option[A]] =
    new Monoid[Option[A]] {
      val isCommutative: Boolean = _isCommutative
      val default: Option[A]     = None

      def combine(optFirst: Option[A], optSecond: Option[A]): Option[A] =
        (optFirst, optSecond) match {
          case (Some(first), Some(second)) => Some(_combine(first, second))
          case (Some(first), None)         => Some(first)
          case (None, Some(second))        => Some(second)
          case (None, None)                => None
        }
    }
}
