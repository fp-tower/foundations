package answers.errorhandling

import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Interval
import exercises.errorhandling.OptionExercises
import toimpl.errorhandling.OptionToImpl

object OptionAnswers extends OptionToImpl {
  def getUser(id: Int, users: List[OptionExercises.User]): Option[OptionExercises.User] =
    users.find(_.id == id)

  def charToDigit(c: Char): Option[Int] =
    refinedCharToDigit(c).map(_.value)

  type Digit = Int Refined Interval.Closed[W.`0`.T, W.`9`.T]
  def refinedCharToDigit(c: Char): Option[Digit] =
    c match {
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
}
