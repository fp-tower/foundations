package answers.dataprocessing

// A Monoid without default value
trait Semigroup[A] {
  // combine is associative
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

  def nSmallest[A: Ordering](n: Int): Semigroup[List[A]] = new Semigroup[List[A]] {
    def combine(first: List[A], second: List[A]): List[A] =
      (first ++ second).sorted.take(n) // can do much better
  }
}
