package exercises.types

final case class Iso[A, B](from: A => B, to: B => A)
