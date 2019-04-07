package exercises.typeclass

case class Endo[A](getEndo: A => A)
