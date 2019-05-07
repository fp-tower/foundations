package exercises.typeclass

case class Min[A](getMin: A)

object Min {
  implicit def eq[A: Eq]: Eq[Min[A]] = Eq.by(_.getMin)
}