package answers.generic

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import scala.util.{Failure, Success, Try}

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

  productPrices.zipWith(productPrices)(List(_, _))
  productPrices.zipWithUncurried[Double, List[Double]](productPrices, List(_, _))

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

  object Predicate {
    def False[A]: Predicate[A] = Predicate(_ => false)
    def True[A]: Predicate[A]  = False.flip

    val FalseVal = False
    val TrueVal  = True
  }

  case class User(name: String, age: Int)

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

  val isCapitalised: Predicate[String] =
    Predicate(word => word.capitalize == word)

  val isValidUserV3: Predicate[User] =
    isBiggerThan(18).contramap[User](_.age) &&
      (isLongerThan(3) && isCapitalised).contramap(_.name)

  val isValidUserV4: Predicate[User] =
    by[User](_.age)(isBiggerThan(18)) &&
      by[User](_.name)(isLongerThan(3) && isCapitalised)

  def by[From]: ByOps[From] = new ByOps[From] {}

  class ByOps[From] { // trick to partially apply type parameters
    def apply[To](zoom: From => To)(predicate: Predicate[To]): Predicate[From] =
      predicate.contramap(zoom)
  }

  ////////////////////////////
  // Exercise 3: JsonDecoder
  ////////////////////////////

  // very basic representation of JSON
  type Json = String

  trait JsonDecoder[A] { outer =>
    def decode(json: Json): A

    def map[To](update: A => To): JsonDecoder[To] =
      new JsonDecoder[To] {
        def decode(json: Json): To =
          update(outer.decode(json))
      }

    def orElse(fallback: JsonDecoder[A]): JsonDecoder[A] =
      (json: Json) =>
        Try(outer.decode(json)) match {
          case Failure(_)     => fallback.decode(json)
          case Success(value) => value
      }
  }

  object JsonDecoder {
    def constant[A](value: A): JsonDecoder[A] = new JsonDecoder[A] {
      def decode(json: Json): A = value
    }

    def fail[A](exception: Exception): JsonDecoder[A] = new JsonDecoder[A] {
      def decode(json: Json): A =
        throw exception
    }
  }

  val intDecoder: JsonDecoder[Int] = new JsonDecoder[Int] {
    def decode(json: Json): Int = json.toInt
  }

  val stringDecoder: JsonDecoder[String] = new JsonDecoder[String] {
    def decode(json: Json): String =
      if (json.startsWith("\"") && json.endsWith("\""))
        json.substring(1, json.length - 1)
      else
        throw new IllegalArgumentException(s"$json is not a JSON string")
  }

  case class UserId(value: Int)
  val userIdDecoder: JsonDecoder[UserId] =
    (json: Json) => UserId(intDecoder.decode(json))

  val localDateDecoder: JsonDecoder[LocalDate] =
    (json: Json) => LocalDate.parse(stringDecoder.decode(json), DateTimeFormatter.ISO_LOCAL_DATE)

  def map[From, To](decoder: JsonDecoder[From])(update: From => To): JsonDecoder[To] =
    (json: Json) => update(decoder.decode(json))

  val userIdDecoderV2: JsonDecoder[UserId] =
    intDecoder.map(UserId)

  val localDateDecoderV2: JsonDecoder[LocalDate] =
    stringDecoder.map(LocalDate.parse(_, DateTimeFormatter.ISO_LOCAL_DATE))

  val longDecoder: JsonDecoder[Long] =
    (json: Json) => json.toLong

  val longLocalDateDecoder: JsonDecoder[LocalDate] =
    longDecoder.map(LocalDate.ofEpochDay)

  val weirdLocalDateDecoder: JsonDecoder[LocalDate] =
    localDateDecoderV2 orElse longLocalDateDecoder

  def optionDecoder[A](decoder: JsonDecoder[A]): JsonDecoder[Option[A]] = {
    case "null" => None
    case other  => Some(decoder.decode(other))
  }

  trait SafeJsonDecoder[A] { self =>
    def decode(json: Json): Either[String, A]

    def map[To](update: A => To): SafeJsonDecoder[To] =
      new SafeJsonDecoder[To] {
        def decode(json: Json): Either[String, To] =
          self.decode(json).map(update)
      }

    def orElse(other: SafeJsonDecoder[A]): SafeJsonDecoder[A] =
      new SafeJsonDecoder[A] {
        def decode(json: Json): Either[String, A] =
          self.decode(json).orElse(other.decode(json))
      }
  }

  object SafeJsonDecoder {
    val int: SafeJsonDecoder[Int] =
      (json: Json) => Try(json.toInt).toOption.toRight(s"Invalid JSON Int: $json")

    val long: SafeJsonDecoder[Long] =
      (json: Json) => Try(json.toLong).toOption.toRight(s"Invalid JSON Long: $json")

    val string: SafeJsonDecoder[String] =
      (json: Json) =>
        if (json.startsWith("\"") && json.endsWith("\""))
          Right(json.substring(1, json.length - 1))
        else
          Left(s"$json is not a JSON string")

    val localDateInt: SafeJsonDecoder[LocalDate] =
      long.map(LocalDate.ofEpochDay)

    val localDateString: SafeJsonDecoder[LocalDate] =
      string.map(LocalDate.parse(_, DateTimeFormatter.ISO_LOCAL_DATE))

    val localDate: SafeJsonDecoder[LocalDate] =
      localDateString.orElse(localDateInt)
  }
}
