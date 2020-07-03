package answers.function

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object GenericFunctionAnswers {

  ////////////////////
  // Exercise 1: Pair
  ////////////////////

  val names: Pair[String] = Pair("John", "Elisabeth")
  val ages: Pair[Int]     = Pair(32, 46)

  case class Pair[A](first: A, second: A) {
    def swap: Pair[A] =
      Pair(second, first)

    def map[To](update: A => To): Pair[To] =
      Pair(update(first), update(second))

    def zipWithUncurried[Other, To](other: Pair[Other], combine: (A, Other) => To): Pair[To] =
      Pair(combine(first, other.first), combine(second, other.second))

    def zipWith[Other, To](other: Pair[Other])(combine: (A, Other) => To): Pair[To] =
      Pair(combine(first, other.first), combine(second, other.second))

    def map3[A2, A3, To](otherB: Pair[A2], otherC: Pair[A3])(combine: (A, A2, A3) => To): Pair[To] =
      zipWith(otherB)((_, _))
        .zipWith(otherC) { case ((a, b), c) => combine(a, b, c) }
  }

  val secret: Pair[List[Byte]] =
    Pair(
      first = List(103, 110, 105, 109, 109, 97, 114, 103, 111, 114, 80),
      second = List(108, 97, 110, 111, 105, 116, 99, 110, 117, 70)
    )
  val decoded: Pair[String] =
    secret
      .map(_.toArray)
      .map(new String(_))
      .map(_.reverse)
      .swap

  case class Product(name: String, price: Double)
  val productNames: Pair[String]  = Pair("Coffee", "Plane ticket")
  val productPrices: Pair[Double] = Pair(2.5, 329.99)
  val products: Pair[Product]     = productNames.zipWith(productPrices)(Product)

  val productsUncurried: Pair[Product] =
    productNames.zipWithUncurried(productPrices, Product.apply)

  productPrices.zipWithUncurried[Double, Double](productPrices, _ + _)

  ////////////////////////////
  // Exercise 2: Predicate
  ////////////////////////////

  val isPositive: Predicate[Int] =
    Predicate((number: Int) => number >= 0)

  val isEven: Predicate[Int] =
    Predicate((number: Int) => number % 2 == 0)

  val isOddPositive: Predicate[Int] =
    isEven.flip && isPositive

  case class Predicate[A](eval: A => Boolean) {
    def apply(value: A): Boolean = eval(value)

    def &&(other: Predicate[A]): Predicate[A] =
      Predicate(value => eval(value) && other.eval(value))

    def ||(other: Predicate[A]): Predicate[A] =
      Predicate(value => eval(value) || other.eval(value))

    def flip: Predicate[A] =
      Predicate(value => !eval(value))

    def contramap[To](update: To => A): Predicate[To] =
      Predicate { (value: To) =>
        val a = update(value)
        eval(a)
      }
  }

  def False[A]: Predicate[A] = Predicate(_ => false)
  def True[A]: Predicate[A]  = False.flip

  val isValidUser: Predicate[User] =
    Predicate(
      user =>
        user.age >= 18 &&
          user.name.length >= 3 &&
          user.name.capitalize == user.name
    )

  val isAdult: Predicate[User] =
    Predicate(_.age >= 18)

  val isUsernameLongerThan3: Predicate[User] =
    Predicate(_.name.length >= 3)

  val isUsernameCapitalised: Predicate[User] =
    Predicate(user => user.name.capitalize == user.name)

  val isValidUserV2: Predicate[User] =
    isAdult && isUsernameLongerThan3 && isUsernameCapitalised

  def isBiggerThan(min: Int): Predicate[Int] =
    Predicate(_ >= min)

  val isAdultV2: Predicate[User] =
    isBiggerThan(18).contramap(_.age)

  val isUsernameLongerThan3V2: Predicate[User] =
    isBiggerThan(3).contramap(_.name.length)

  def isLongerThan(min: Int): Predicate[String] =
    isBiggerThan(min).contramap(_.length)

  case class User(name: String, age: Int)

  val isCapitalised: Predicate[String] =
    Predicate(word => word.capitalize == word)

  val isValidUserV3: Predicate[User] =
    isBiggerThan(18).contramap[User](_.age) &&
      (isLongerThan(3) && isCapitalised).contramap(_.name)

  val isValidUserV4: Predicate[User] =
    by[User](_.age)(isBiggerThan(18)) &&
      by[User](_.name)(isLongerThan(3) && isCapitalised)

  def by[From]: Foo[From] = new Foo[From] {}

  class Foo[A] {
    def apply[B](f: A => B)(predicate: Predicate[B]): Predicate[A] =
      predicate.contramap(f)
  }

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
  // such as userIdDecoder.decoder(UserId("1234")) == 1234
  // Note: Try to re-use `intDecoder` defined below.
  case class UserId(id: Int)
  val userIdDecoder: JsonDecoder[UserId] = new JsonDecoder[UserId] {
    def decode(json: Json): UserId =
      UserId(intDecoder.decode(json))
  }

  // 3b. Implement `localDateDecoder`, a `JsonDecoder` for `LocalDate`
  // such as userIdDecoder.decoder("2020-26-03") == LocalDate.of(2020,26,03)
  // Note: You can parse a `LocalDate` using `LocalDate.parse` with a java.time.format.DateTimeFormatter
  //       Try to re-use `stringDecoder` defined below.
  val localDateDecoder: JsonDecoder[LocalDate] = new JsonDecoder[LocalDate] {
    def decode(json: Json): LocalDate =
      LocalDate.parse(stringDecoder.decode(json), DateTimeFormatter.ISO_LOCAL_DATE)
  }

  // 3c. Implement `map` a generic method that converts a `JsonDecoder`
  // of one type into a `JsonDecoder` of another type.
  def map[From, To](decoder: JsonDecoder[From], update: From => To): JsonDecoder[To] =
    new JsonDecoder[To] {
      def decode(json: Json): To =
        update(decoder.decode(json))
    }

  // 3d. Re-implement a `JsonDecoder` for `UserId and `LocalDate` using `map`
  lazy val userIdDecoderV2: JsonDecoder[UserId] =
    map(intDecoder, (x: Int) => UserId(x))

  lazy val localDateDecoderV2: JsonDecoder[LocalDate] =
    map(stringDecoder, (text: String) => LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE))

  // 3e. (difficult) How would you define and implement a `JsonDecoder` for a generic `Option`?
  // such as we can decode:
  // * "1" into a Some(1)
  // * "2020-26-03" into a Some(LocalDate.of(2020,26,03))
  // * "null" into "None"
  def optionDecoder[A](decoder: JsonDecoder[A]): JsonDecoder[Option[A]] =
    new JsonDecoder[Option[A]] {
      def decode(json: Json): Option[A] =
        json match {
          case "null" => None
          case other  => Some(decoder.decode(json))
        }
    }
}
