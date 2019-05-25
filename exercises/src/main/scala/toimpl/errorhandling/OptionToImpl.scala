package toimpl.errorhandling

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Interval
import exercises.errorhandling.OptionExercises.User

trait OptionToImpl {

  def getUser(id: Int, users: List[User]): Option[User]

  def charToDigit(c: Char): Option[Int]

  def refinedCharToDigit(c: Char): Option[Int Refined Interval.Closed[W.`0`.T, W.`9`.T]]

}
