package exercises.typeclass

case class Dual[A](getDual: A)

object Dual {
  implicit def eq[A: Eq]: Eq[Dual[A]] = Eq.by(_.getDual)
}
