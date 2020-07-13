package exercises.valfunction

object ValueFunctionExercises {

  /////////////////////////////////////////////////////
  // Exercise 1: String API with higher-order functions
  /////////////////////////////////////////////////////

  // 1a. Implement `selectDigits` which iterates over a String and only keep the characters that are digits.
  // such as selectDigits("hello4world-80") == "480"
  // but     selectDigits("welcome") == ""
  // Note: You can use `filter` method from `String`, also check out the API of `Char`
  def selectDigits(text: String): String =
    ???

  // 1b. Implement `secret` which transforms all characters in a String to '*'
  // such as secret("Welcome123") == "**********"
  // Note: Try to use a higher-order function from the String API
  def secret(text: String): String =
    ???

  // 1c. Implement `isValidUsernameCharacter` which checks if a character is suitable for a username.
  // We accept:
  // - lower and upper case letters
  // - digits
  // - special characters: '-' and '_'
  // For example, isValidUsernameCharacter('3') == true
  //              isValidUsernameCharacter('a') == true
  // but          isValidUsernameCharacter('^') == false
  // Note: You might find some useful helper methods on `char`.
  def isValidUsernameCharacter(char: Char): Boolean =
    ???

  // 1d. Implement `isValidUsername` which checks that all the characters in a String are valid
  // such as isValidUsername("john-doe") == true
  // but     isValidUsername("*john*") == false
  // Note: Try to use `isValidUsernameCharacter` and a higher-order function from the String API.
  def isValidUsername(username: String): Boolean =
    ???

  ///////////////////////
  // Exercise 2: Point
  ///////////////////////

  case class Point(x: Int, y: Int, z: Int) {
    // 2a. Implement `isPositive` which returns true if `x`, `y` and `z` are all greater or equal to 0, false otherwise
    // such as Point(2, 4,9).isPositive == true
    //         Point(0, 0,0).isPositive == true
    // but     Point(0,-2,1).isPositive == false
    // Note: `isPositive` is a function defined within `Point` class, so `isPositive` has access to `x`, `y` and `z`.
    def isPositive: Boolean =
      ???

    // 2b. Implement `isEven` which returns true if `x`, `y` and `z` are all even numbers, false otherwise
    // such as Point(2, 4, 8).isEven == true
    //         Point(0,-8,-2).isEven == true
    // but     Point(3,-2, 0).isEven == false
    // Note: You can use `% 2` to check if a number is odd or even,
    // e.g. 8 % 2 == 0 but 7 % 2 == 1
    def isEven: Boolean =
      ???

    // 2c. Both `isPositive` and `isEven` check that a predicate holds for `x`, `y` and `z`.
    // Let's try to capture this pattern with a higher order function like `forAll`
    // such as Point(1,1,1).forAll(_ == 1) == true
    // but     Point(1,2,5).forAll(_ == 1) == false
    // Then, re-implement `isPositive` and `isEven` using `forAll`
    def forAll(predicate: Int => Boolean): Boolean =
      ???
  }
}
