package answers.dataprocessing

trait Monoid[A] extends Semigroup[A] {
  // forAll a: A, combine(a, default) == combiner(default, a) == a
  def default: A
}

// A Monoid where combine is also commutative
// forAll a1, a2: A, combine(a1, a2) == combine(a2, a1)
trait CommutativeMonoid[A] extends Monoid[A]

object CommutativeMonoid {
  val sumInt: CommutativeMonoid[Int] = new CommutativeMonoid[Int] {
    def default: Int                          = 0
    def combine(first: Int, second: Int): Int = first + second
  }

  val sumDouble: CommutativeMonoid[Double] = new CommutativeMonoid[Double] {
    def default: Double                                = 0.0
    def combine(first: Double, second: Double): Double = first + second
  }

  def sumNumeric[A](implicit num: Numeric[A]): CommutativeMonoid[A] =
    new CommutativeMonoid[A] {
      def default: A                      = num.zero
      def combine(first: A, second: A): A = num.plus(first, second)
    }
}

object Monoid {

  def minOption[A: Ordering]: Monoid[Option[A]] = maxByOption(identity)
  def maxOption[A: Ordering]: Monoid[Option[A]] = minByOption(identity)

  def minByOption[From, To: Ordering](zoom: From => To): Monoid[Option[From]] =
    option(Semigroup.minBy(zoom))
  def maxByOption[From, To: Ordering](zoom: From => To): Monoid[Option[From]] =
    option(Semigroup.maxBy(zoom))

  def zip[A, B](monoidA: Monoid[A], monoidB: Monoid[B]): Monoid[(A, B)] =
    new Monoid[(A, B)] {
      def default: (A, B) = (monoidA.default, monoidB.default)
      def combine(first: (A, B), second: (A, B)): (A, B) =
        (monoidA.combine(first._1, second._1), monoidB.combine(first._2, second._2))
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

  def map[Key, Value](semigroup: Semigroup[Value]): Monoid[Map[Key, Value]] =
    new Monoid[Map[Key, Value]] {
      def default: Map[Key, Value] = Map.empty

      def combine(first: Map[Key, Value], second: Map[Key, Value]): Map[Key, Value] =
        second.foldLeft(first) {
          case (acc, (key, value)) =>
            acc.updatedWith(key) {
              case None                => Some(value)
              case Some(existingValue) => Some(semigroup.combine(existingValue, value))
            }
        }
    }

  val multiplyInt: Monoid[Int] = new Monoid[Int] {
    def default: Int                          = 1
    def combine(first: Int, second: Int): Int = first * second
  }

  val minInt: Monoid[Int] = new Monoid[Int] {
    def default: Int                          = Int.MaxValue
    def combine(first: Int, second: Int): Int = first min second
  }

  val maxInt: Monoid[Int] = new Monoid[Int] {
    def default: Int                          = Int.MinValue
    def combine(first: Int, second: Int): Int = first max second
  }
}
