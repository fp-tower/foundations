package exercises.functors

import exercises.typeclass.Eq

case class Const[A, B](getConst: A) {
  def as[C]: Const[A, C] = Const(getConst)
}

object Const {
  implicit def eq[A: Eq, B]: Eq[Const[A, B]] = Eq.by(_.getConst)
}