package exercises.types

case class Iso[A, B](from: A => B, to: B => A)
