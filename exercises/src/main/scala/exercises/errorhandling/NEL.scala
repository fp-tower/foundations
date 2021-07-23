package exercises.errorhandling

// Non-Empty List
// It is called `NonEmptyList` in cats and zio-prelude.
// There is not an exact equivalent to Nel in the standard library.
// The closest is `::`, the cons constructor of List.
case class NEL[+A](head: A, tail: List[A]) {
  def toList: List[A] =
    head :: tail

  def map[Next](update: A => Next): NEL[Next] =
    NEL(update(head), tail.map(update))

  // concat
  def ++[Other >: A](other: NEL[Other]): NEL[Other] =
    NEL(head, tail ++ other.toList)

  // prepend
  def +:[Other >: A](other: Other): NEL[Other] =
    NEL(other, head +: tail)

  // append
  def :+[Other >: A](other: Other): NEL[Other] =
    NEL(head, tail :+ other)
}

object NEL {
  def apply[A](head: A, tail: A*): NEL[A] =
    NEL(head, tail.toList)

  def one[A](value: A): NEL[A] =
    NEL(value)

  def fromList[A](values: List[A]): Option[NEL[A]] =
    values match {
      case Nil          => None
      case head :: next => Some(NEL(head, next))
    }
}
