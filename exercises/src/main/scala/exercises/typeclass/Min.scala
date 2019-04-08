package exercises.typeclass

import cats.kernel.Eq

case class Min[A](getMin: A)

object Min {
  implicit def eq[A: Eq]: Eq[Min[A]] = Eq.by(_.getMin)
}