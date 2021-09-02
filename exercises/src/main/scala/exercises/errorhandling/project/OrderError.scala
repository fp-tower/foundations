package exercises.errorhandling.project

sealed trait OrderError
object OrderError {
  case object EmptyBasket                         extends OrderError
  case class InvalidStatus(currentStatus: String) extends OrderError
}
