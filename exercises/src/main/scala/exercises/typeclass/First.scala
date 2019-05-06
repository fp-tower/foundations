package exercises.typeclass

case class First[A](getFirst: A)

object First {
  implicit def eq[A: Eq]: Eq[First[A]] = Eq.by(_.getFirst)
}