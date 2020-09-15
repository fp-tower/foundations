package answers.dataprocessing

trait Semigroup[A] {
  // combine is associative (this means you can move parentheses around)
  // forAll a1, a2, a3: A, combine(a1, combine(a2, a3)) == combine(combine(a1, a2), a3)
  def combine(first: A, second: A): A
}

object Semigroup {
  def min[A: Ordering]: Semigroup[A] = minBy(identity)
  def max[A: Ordering]: Semigroup[A] = maxBy(identity)

  def minBy[From, To: Ordering](zoom: From => To): Semigroup[From] = new Semigroup[From] {
    def combine(first: From, second: From): From =
      Ordering.by(zoom).min(first, second)
  }

  def maxBy[From, To: Ordering](zoom: From => To): Semigroup[From] = new Semigroup[From] {
    def combine(first: From, second: From): From =
      Ordering.by(zoom).max(first, second)
  }
}
