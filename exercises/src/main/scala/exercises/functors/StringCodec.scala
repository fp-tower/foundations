package exercises.functors

case class StringCodec[A](mkString: A => String, parse: String => Option[A])
