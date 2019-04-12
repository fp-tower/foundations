package exercises.typeclass2

import cats.Eq

case class Id[A](value: A)

object Id {
  implicit def eq[A: Eq]: Eq[Id[A]] = Eq.by(_.value)
}
