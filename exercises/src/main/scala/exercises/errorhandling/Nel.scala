package exercises.errorhandling

// Non-Empty List
// It is called `NonEmptyList` in cats and zio-prelude.
// There is not an exact equivalent to Nel in the standard library.
// The closest is `::`, the cons constructor of List.
case class Nel[+A](head: A, tail: List[A]) {
  def toList: List[A] =
    head :: tail

  // concat
  def ++[Other >: A](other: Nel[Other]): Nel[Other] =
    Nel(head, tail ++ other.toList)

  // prepend
  def +:[Other >: A](other: Other): Nel[Other] =
    Nel(other, head +: tail)

  // append
  def :+[Other >: A](other: Other): Nel[Other] =
    Nel(head, tail :+ other)
}

object Nel {
  def apply[A](head: A, tail: A*): Nel[A] =
    Nel(head, tail.toList)

  def one[A](value: A): Nel[A] =
    Nel(value)

  def fromList[A](values: List[A]): Option[Nel[A]] =
    values match {
      case Nil          => None
      case head :: next => Some(Nel(head, next))
    }
}
