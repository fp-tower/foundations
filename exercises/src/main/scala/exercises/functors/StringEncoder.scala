package exercises.functors

case class StringEncoder[A](mkString: A => String)
