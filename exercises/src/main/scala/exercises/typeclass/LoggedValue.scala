package exercises.typeclass

case class LoggedValue[A](value: A, comments: List[String])
