package answers.valfunction

object ValueFunctionAnswers {

  /////////////////////////////////////////////////////
  // Exercise 1: String API with higher-order functions
  /////////////////////////////////////////////////////

  def selectDigits(text: String): String =
    text.filter(_.isDigit)

  def secret(text: String): String =
    text.map(_ => '*')

  def isValidUsernameCharacter(char: Char): Boolean =
    char.isLetterOrDigit || (char == '-') || (char == '_')

  def isValidUsername(username: String): Boolean =
    username.forall(isValidUsernameCharacter)

  ///////////////////////
  // Exercise 2: Point3
  ///////////////////////

  case class Point(x: Int, y: Int, z: Int) {
    def isPositive: Boolean =
      x >= 0 && y >= 0 && z >= 0

    def isEven: Boolean =
      (x % 2 == 0) && (y % 2 == 0) && (z % 2 == 0)

    def forAll(predicate: Int => Boolean): Boolean =
      predicate(x) && predicate(y) && predicate(z)

    def isPositiveForAll: Boolean =
      forAll(_ >= 0)

    def isEvenForAll: Boolean =
      forAll(_ % 2 == 0)
  }
}
