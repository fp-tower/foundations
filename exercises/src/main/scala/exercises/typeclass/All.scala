package exercises.typeclass

case class All(getAll: Boolean)

object All {
  implicit val eq: Eq[All] = Eq.by[All, Boolean](_.getAll)
}