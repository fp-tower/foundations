package answers.types

sealed trait Comparison
object Comparison {
  case object LessThan    extends Comparison
  case object EqualTo     extends Comparison
  case object GreaterThan extends Comparison
}
