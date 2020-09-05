package answers.dataprocessing

trait Monoid[A] extends Semigroup[A] {
  // forAll a: A, combine(a, default) == combiner(defaut, a) == a
  def default: A
}

object Monoid {
  def minOption[A: Ordering]: Monoid[Option[A]] = maxByOption(identity)
  def maxOption[A: Ordering]: Monoid[Option[A]] = minByOption(identity)

  def minByOption[From, To: Ordering](zoom: From => To): Monoid[Option[From]] =
    option(Semigroup.minBy(zoom))
  def maxByOption[From, To: Ordering](zoom: From => To): Monoid[Option[From]] =
    option(Semigroup.maxBy(zoom))

  def sumNumeric[A](implicit num: Numeric[A]): Monoid[A] =
    new Monoid[A] {
      def default: A                      = num.zero
      def combine(first: A, second: A): A = num.plus(first, second)
    }

  def tuple2[A, B](a: Monoid[A], b: Monoid[B]): Monoid[(A, B)] =
    new Monoid[(A, B)] {
      def default: (A, B) = (a.default, b.default)
      def combine(first: (A, B), second: (A, B)): (A, B) =
        (a.combine(first._1, second._1), b.combine(first._2, second._2))
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
}
