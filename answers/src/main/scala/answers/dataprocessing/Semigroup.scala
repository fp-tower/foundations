package answers.dataprocessing

trait Semigroup[A] {
  def isCommutative: Boolean

  def combine(first: A, second: A): A
}

object Semigroup {
  val min: Semigroup[Double] = new Semigroup[Double] {
    def isCommutative: Boolean                         = true
    def combine(first: Double, second: Double): Double = first min second
  }

  val max: Semigroup[Double] = new Semigroup[Double] {
    def isCommutative: Boolean                         = true
    def combine(first: Double, second: Double): Double = first max second
  }
}
