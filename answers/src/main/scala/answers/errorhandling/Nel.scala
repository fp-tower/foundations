package answers.errorhandling

// Non Empty List
case class Nel[+A](head: A, tail: List[A]) {
  def toList: List[A] =
    head :: tail

  def ++[Other >: A](other: Nel[Other]): Nel[Other] =
    Nel(head, tail ++ other.toList)

  def :+[Other >: A](other: Other): Nel[Other] =
    Nel(head, tail :+ other)
}

object Nel {
  def apply[A](head: A, tail: A*): Nel[A] =
    Nel(head, tail.toList)

  def one[A](value: A): Nel[A] =
    Nel(value)
}
