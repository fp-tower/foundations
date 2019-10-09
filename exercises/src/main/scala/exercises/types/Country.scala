package exercises.types

sealed trait Country

object Country {
  case object France        extends Country
  case object Germany       extends Country
  case object UnitedKingdom extends Country
}
