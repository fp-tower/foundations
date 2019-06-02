package exercises.errorhandling

case class User(userName: Username, country: Country)

case class Username(value: String)

sealed trait Country

object Country {
  val all: List[Country] = List(France, Germany, Switzerland, UnitedKingdom)

  case object France        extends Country
  case object Germany       extends Country
  case object Switzerland   extends Country
  case object UnitedKingdom extends Country
}
