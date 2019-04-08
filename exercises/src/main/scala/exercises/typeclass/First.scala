package exercises.typeclass

import cats.kernel.Eq

case class First[A](getFirst: A)

object First {
  implicit def eq[A: Eq]: Eq[First[A]] = Eq.by(_.getFirst)
}