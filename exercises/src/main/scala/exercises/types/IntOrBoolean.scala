package exercises.types

sealed trait IntOrBoolean

object IntOrBoolean {
  case class AnInt(value: Int)        extends IntOrBoolean
  case class ABoolean(value: Boolean) extends IntOrBoolean
}
