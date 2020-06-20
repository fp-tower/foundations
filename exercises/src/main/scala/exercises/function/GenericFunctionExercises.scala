package exercises.function

import java.time.LocalDate

object GenericFunctionExercises {

  ////////////////////
  // Exercise 1: Pair
  ////////////////////

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

    // 1c. Implement `zipWith` which merges two `Pair` using a `combine` function
    // such as Pair(0, 2).zipWith(Pair(3, 3))((x, y) => x + y) == Pair(3, 5)
    //         Pair(2, 3).zipWith(Pair("Hello ", "World "))(replicate) == Pair("Hello Hello ", "World World World ")
    // Bonus: Why did we separate the arguments of `zipWith` into two set of parentheses?
    def zipWith[Other, To](other: Pair[Other])(combine: (A, Other) => To): Pair[To] =
      ???
  }

  // 1d. Use the Pair API to decode the content of `secret`.
  // Hint: it is the subject of the course
  // Note: you can remove the lazy keyword from `decoded` once you have implemented it.
  val secret: Pair[String]       = Pair("gnimmargorP", "lanoitcnuF")
  lazy val decoded: Pair[String] = ???

  // 1e. Use the Pair API to combine `productNames` and `productPrices` into `products`
  // such as products == Pair(Product("Coffee", 2.5), Product("Plane ticket", 329.99))
  case class Product(name: String, price: Double)
  val productNames: Pair[String]  = Pair("Coffee", "Plane ticket")
  val productPrices: Pair[Double] = Pair(2.5, 329.99)
  lazy val products: Pair[Product] =
    ???

  // 1f. Can you implement a method on `Pair` similar to `zipWith`, but that combines
  // 3 `Pair` instead of 2? If yes, can you implement this method using `zipWith`?

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

  // 2e. Implement `isLongerThan`, a predicate that checks if a text is longer than a constant
  // such as isLongerThan(5)("hello") == true
  // but     isLongerThan(5)("hey")   == false
  def isLongerThan(min: Int): Predicate[String] =
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
  case class User(name: String, age: Int)
  lazy val isValidUser: Predicate[User] =
    ???

  // 2h. Could you generalise `isAdult` and `longerThan`?
  // Try to define a function that will help you re-implement both.

  ////////////////////////////
  // Exercise 3: JsonDecoder
  ////////////////////////////

  // very basic representation of JSON
  type Json = String

  trait JsonDecoder[A] {
    def decode(json: Json): A
  }

  val stringDecoder: JsonDecoder[String] = new JsonDecoder[String] {
    def decode(json: Json): String =
      if (json.startsWith("\"") && json.endsWith("\""))
        json.substring(1, json.length - 1)
      else
        throw new IllegalArgumentException(s"$json is not a JSON string")
  }
  val intDecoder: JsonDecoder[Int] = new JsonDecoder[Int] {
    def decode(json: Json): Int = json.toInt
  }

  // 3a. Implement `userIdDecoder`, a `JsonDecoder` for `UserId`
  // such as userIdDecoder.decode("1234") == UserId(1234)
  // Note: Try to re-use `intDecoder` defined below.
  case class UserId(id: Int)
  val userIdDecoder: JsonDecoder[UserId] = new JsonDecoder[UserId] {
    def decode(json: Json): UserId =
      ???
  }

  // 3b. Implement `localDateDecoder`, a `JsonDecoder` for `LocalDate`
  // such as localDateDecoder.decode("2020-03-26") == LocalDate.of(2020,3,26)
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

  // 3e. How would you define and implement a `JsonDecoder` for a generic `Option`?
  // such as we can decode:
  // * "1" into a Some(1)
  // * "2020-26-03" into a Some(LocalDate.of(2020,26,03))
  // * "null" into "None"
  def optionDecoder[A]: JsonDecoder[Option[A]] =
    ???

}
