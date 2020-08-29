package answers.dataprocessing

trait Semigroup[A] {
  def combine(first: A, second: A): A
}

object Semigroup {
  val min: Semigroup[Double] = new Semigroup[Double] {
    def combine(first: Double, second: Double): Double = first min second
  }

  val max: Semigroup[Double] = new Semigroup[Double] {
    def combine(first: Double, second: Double): Double = first max second
  }

  def minBy[From, To: Ordering](zoom: From => To): Semigroup[From] = new Semigroup[From] {
    def combine(first: From, second: From): From =
      Ordering.by(zoom).min(first, second)
  }

  def maxBy[From, To: Ordering](zoom: From => To): Semigroup[From] = new Semigroup[From] {
    def combine(first: From, second: From): From =
      Ordering.by(zoom).max(first, second)
  }
}
