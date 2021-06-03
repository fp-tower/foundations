package answers.errorhandling.option

object Exercise3 {

  def charToDigit(character: Char): Option[Int] =
    character match {
      case '0' => Some(0)
      case '1' => Some(1)
      case '2' => Some(2)
      case '3' => Some(3)
      case '4' => Some(4)
      case '5' => Some(5)
      case '6' => Some(6)
      case '7' => Some(7)
      case '8' => Some(8)
      case '9' => Some(9)
      case _   => None
    }

  def filterDigits(characters: List[Char]): List[Int] =
    characters.flatMap(charToDigit(_).toList)

  def checkAllDigits(characters: List[Char]): Option[List[Int]] =
    characters.traverse(charToDigit)

  def sequence[A](options: List[Option[A]]): Option[List[A]] =
    options
      .foldLeft(Option(List.empty[A])) { (state, option) =>
        state.zip(option).map { case (list, value) => value :: list }
      }
      .map(_.reverse)

  def traverse[A, B](values: List[A])(transform: A => Option[B]): Option[List[B]] =
    sequence(values.map(transform))

}
