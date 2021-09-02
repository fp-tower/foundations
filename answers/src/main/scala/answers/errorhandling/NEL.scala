package answers.errorhandling

// Non-Empty List
case class NEL[+A](head: A, tail: List[A]) {
  def toList: List[A] =
    head :: tail

  def map[Next](update: A => Next): NEL[Next] =
    NEL(update(head), tail.map(update))

  def ++[Other >: A](other: NEL[Other]): NEL[Other] =
    NEL(head, tail ++ other.toList)

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
