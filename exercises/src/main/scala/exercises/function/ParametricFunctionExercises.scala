package exercises.function

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

  // 1f. Use Pair API to combine `names` and `ages` into `users`
  // such as `users` is equal to Pair(User("John", 32), User("Elisabeth", 46))
  lazy val users: Pair[User] =
    ???

  // 1g. Use Pair API to check the length of both String in `names` are strictly longer than 5
  lazy val longerThan5: Boolean =
    ???

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
    Predicate((x: Int) => ???)

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

  ////////////////////////////
  // Exercise 3: JsonEncoder
  ////////////////////////////

  // 3a. Implement `userIdEncoder`, a `JsonEncoder` for `UserId`
  // such as userIdEncoder.encoder(UserId(1234)) == "1234"
  // Note: Try to re-use `intEncoder` defined below.
  case class UserId(id: Int)
  val userIdEncoder: JsonEncoder[UserId] = new JsonEncoder[UserId] {
    def encode(value: UserId): Json = ???
  }

  // 3b. Implement `localDateEncoder`, a `JsonEncoder` for `LocalDate`
  // such as userIdEncoder.encoder(LocalDate.of(2020,26,03)) == "2020-26-03"
  // Note: You can format a `LocalDate` using a java.time.format.DateTimeFormatter
  //       Try to re-use `stringEncoder` defined below.
  val localDateEncoder: JsonEncoder[LocalDate] = new JsonEncoder[LocalDate] {
    def encode(value: LocalDate): Json = ???
  }

  // very basic representation of JSON
  type Json = String

  trait JsonEncoder[A] {
    def encode(value: A): Json
  }

  val intEncoder: JsonEncoder[Int] = new JsonEncoder[Int] {
    def encode(value: Int): Json = value.toString
  }
  val stringEncoder: JsonEncoder[String] = new JsonEncoder[String] {
    def encode(value: String): Json = value
  }

  // 3c. Implement `contraMap` a generic method that converts a `JsonEncoder`
  // of one type into a `JsonEncoder` of another type.
  def contraMap[From, To](encoder: JsonEncoder[From], update: To => From): JsonEncoder[To] =
    new JsonEncoder[To] {
      def encode(value: To): Json =
        ???
    }

  // 3d. Re-implement a `JsonEncoder` for `UserId and `LocalDate` using `contraMap`
  lazy val userIdEncoderV2: JsonEncoder[UserId] =
    ???

  lazy val localDateEncoderV2: JsonEncoder[LocalDate] =
    ???

  // 3e. How would you define and implement a `JsonEncoder` for generic `List`?
  // For example, we should be able to use `listEncoder` to encode a `List[Int]`,
  // `List[String]` or `List[LocaDate]`.
  def listEncoder[A]: JsonEncoder[List[A]] =
    ???

}
