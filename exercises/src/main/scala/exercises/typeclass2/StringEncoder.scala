package exercises.typeclass2

case class StringEncoder[A](mkString: A => String)
