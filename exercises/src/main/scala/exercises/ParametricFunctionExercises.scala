package exercises

import java.time.LocalDate

object ParametricFunctionExercises {

  ////////////////////////////
  // Exercise 1: Pair
  ////////////////////////////

  val names: Pair[String] = Pair("John", "Elisabeth")
  val ages: Pair[Int]     = Pair(32, 46)

  case class Pair[A](first: A, second: A) {
    // 1a. Implement `swap` which exchanges `first` and `second`
    // such as Pair("John", "Doe").swap == Pair("Doe", "John")
    def swap: Pair[A] =
      ???

    // 1b. Implement `map` which applies a function to `first` and `second`
    // such as Pair("John", "Doe").map(_.length) == Pair(4,3)
    def map[To](update: A => To): Pair[To] =
      ???

    // 1c. Implement `forAll` which check if a predicate is true for both `first` and `second`
    // such as Pair(2, 6).forAll(_ > 0) == true
    // but     Pair(2, 6).forAll(_ > 2) == false
    //         Pair(2, 6).forAll(_ > 9) == false
    def forAll(predicate: A => Boolean): Boolean =
      ???

    // 1d. Implement `zipWith` which merges two `Pair` using a `combine` function
    // such as Pair(0, 2).zipWith(Pair(3, 3), (x: Int, y: Int) => x + y) == Pair(3, 5)
    //         Pair(2, 3).zipWith(Pair("Hello ", "World "), replicate) == Pair("Hello Hello ", "World World World ")
    def zipWith[B, To](other: Pair[B], combine: (A, B) => To): Pair[To] =
      ???
  }

  case class User(name: String, age: Int)

  // 1f. Use Pair API to check the length of both String in `names` (defined at the beginning of the exercise)
  // are strictly longer than 5.
  lazy val longerThan5: Boolean =
    ???

  // 1g. Use Pair API to combine `names` and `ages` into `users`
  // such as `users` is equal to Pair(User("John", 32), User("Elisabeth", 46))
  lazy val users: Pair[User] =
    ???

  // 1h. (difficult) Can you implement a method on `Pair` similar to `zipWith`, but it combines 3 `Pair`
  // instead of 2? If yes, can you implement this method using `zipWith`?

  ////////////////////////////
  // Exercise 2: Predicate
  ////////////////////////////

  case class Predicate[A](eval: A => Boolean) {
    // DSL to call a predicate like a function
    // isEven(10) instead of isEven.eval(10)
    def apply(value: A): Boolean = eval(value)

    // 2a. Implement `&&` that combines two predicates using logical and
    // such as (isEven && isBiggerThan(10))(12) == true
    // but     (isEven && isBiggerThan(10))(11) == false
    //         (isEven && isBiggerThan(10))(8)  == false
    def &&(other: Predicate[A]): Predicate[A] =
      ???

    // 2b. Implement `||` that combines two predicates using logical or
    // such as (isEven || isBiggerThan(10))(11) == true
    //         (isEven || isBiggerThan(10))(8)  == true
    // but     (isEven || isBiggerThan(10))(7)  == false
    def ||(other: Predicate[A]): Predicate[A] =
      ???

    // 2c. Implement `flip` that reverses a predicate
    // such as isEven.flip(11) == true
    def flip: Predicate[A] =
      ???
  }

  // 2d. Implement `isAdult`, a predicate that checks if an number is bigger than 18
  // such as isAdult(20) == true
  // but     isAdult(6)  == false
  val isAdult: Predicate[Int] =
    Predicate((age: Int) => ???)

  // 2e. Implement `longerThan`, a predicate that checks if a text is longer than a constant
  // such as longerThan(5)("hello") == true
  // but     longerThan(5)("hey")   == false
  def longerThan(min: Int): Predicate[String] =
    Predicate((text: String) => ???)

  // 2f. Implement `contains`, a predicate that checks if a character is present in a text
  // such as contains('l')("hello") == true
  // but     contains('z')("hello") == false
  def contains(char: Char): Predicate[String] =
    Predicate((text: String) => ???)

  // 2g. Implement `isValidUser` which checks if a `User` is:
  // * an adult and
  // * his name is longer than 3 characters
  // Note: Try to re-use `isAdult` and `longerThan`
  lazy val isValidUser: Predicate[User] =
    ???

  // 2h. (difficult) Could you generalise `isAdult` and `longerThan`?

  ////////////////////////////
  // Exercise 3: JsonDecoder
  ////////////////////////////

  // very basic representation of JSON
  type Json = String

  trait JsonDecoder[A] {
    def decode(json: Json): A
  }

  val stringDecoder: JsonDecoder[String] = new JsonDecoder[String] {
    def decode(json: Json): String = json
  }
  val intDecoder: JsonDecoder[Int] = new JsonDecoder[Int] {
    def decode(json: Json): Int = json.toInt
  }

  // 3a. Implement `userIdDecoder`, a `JsonDecoder` for `UserId`
  // such as userIdDecoder.decoder(UserId("1234")) == 1234
  // Note: Try to re-use `intDecoder` defined below.
  case class UserId(id: Int)
  val userIdDecoder: JsonDecoder[UserId] = new JsonDecoder[UserId] {
    def decode(json: Json): UserId =
      ???
  }

  // 3b. Implement `localDateDecoder`, a `JsonDecoder` for `LocalDate`
  // such as userIdDecoder.decoder("2020-26-03") == LocalDate.of(2020,26,03)
  // Note: You can parse a `LocalDate` using `LocalDate.parse` with a java.time.format.DateTimeFormatter
  //       Try to re-use `stringDecoder` defined below.
  lazy val localDateDecoder: JsonDecoder[LocalDate] =
    ???

  // 3c. Implement `map` a generic method that converts a `JsonDecoder`
  // of one type into a `JsonDecoder` of another type.
  def map[From, To](decoder: JsonDecoder[From], update: From => To): JsonDecoder[To] =
    ???

  // 3d. Re-implement a `JsonDecoder` for `UserId and `LocalDate` using `map`
  lazy val userIdDecoderV2: JsonDecoder[UserId] =
    ???

  lazy val localDateDecoderV2: JsonDecoder[LocalDate] =
    ???

  // 3e. (difficult) How would you define and implement a `JsonDecoder` for a generic `Option`?
  // such as we can decode:
  // * "1" into a Some(1)
  // * "2020-26-03" into a Some(LocalDate.of(2020,26,03))
  // * "null" into "None"
  def optionDecoder[A]: JsonDecoder[Option[A]] =
    ???

}
