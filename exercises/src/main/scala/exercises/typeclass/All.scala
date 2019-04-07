package exercises.typeclass

import cats.kernel.Eq
import cats.implicits._

case class All(getAll: Boolean)

object All {
  implicit val eq: Eq[All] = Eq.by[All, Boolean](_.getAll)
}