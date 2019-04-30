package exercises.functors

case class Predicate[A](condition: A => Boolean)
