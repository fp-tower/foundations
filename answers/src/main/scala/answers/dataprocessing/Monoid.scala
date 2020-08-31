package answers.dataprocessing

trait Monoid[A] extends Semigroup[A] {
  // forAll a: A, combine(a, default) == combiner(defaut, a) == a
  def default: A
}

object Monoid {
  def maxOption[A: Ordering]: Monoid[Option[Double]] = option(Semigroup.max)
  def minOption[A: Ordering]: Monoid[Option[Double]] = option(Semigroup.min)

  def sumNumeric[A](implicit num: Numeric[A]): Monoid[A] =
    new Monoid[A] {
      def default: A                      = num.zero
      def combine(first: A, second: A): A = num.plus(first, second)
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
