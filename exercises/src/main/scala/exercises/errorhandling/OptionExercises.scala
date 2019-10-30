package exercises.errorhandling

import answers.sideeffect.IOAnswers.IO
import exercises.errorhandling.OptionExercises.Shape.{Circle, Rectangle}
import io.circe.{parser, Json}

import scala.concurrent.duration._
import scala.util.Try

object OptionExercises {

  ////////////////////////
  // 1. Use cases
  ////////////////////////

  // 1a. Implement `getUserEmail` which looks up a user using its id, then it returns the user's email if it exists.
  // val userMap = Map(
  //   222 -> User(222, "john" , "j@x.com"),
  //   123 -> User(123, "elisa", "e@y.com"),
  //   444 -> User(444, "bob")
  // )
  // getUserEmail(123, userMap) == Some("e@y.com")
  // getUserEmail(111, userMap) == None // no user
  // getUserEmail(444, userMap) == None // no email
  def getUserEmail(id: UserId, users: Map[UserId, User]): Option[Email] = ???

  case class User(id: UserId, name: String, email: Option[Email])
  case class UserId(value: Long)
  case class Email(value: String)

  sealed trait Role {
    import Role._

    // 1b. Implement `optSingleAccountId` which returns the account id if the role is a Reader or Editor
    // such as Editor(123, "Comic Sans").optSingleAccountId == Some(123)
    //         Reader(123, premiumUser = true).optSingleAccountId == Some(123)
    // but     Admin.optSingleAccountId == None
    // Note: you can pattern match on Role using `this match { case Reader(...) => ... }`
    def optSingleAccountId: Option[AccountId] = ???

    // 1c. Implement `optEditor` which checks if the current `Role` is an `Editor`
    // such as Editor(123, "Comic Sans").optEditor == Some(Editor(123, "Comic Sans"))
    // but     Reader(123, premiumUser = true).optEditor == None
    def optEditor: Option[Role.Editor] = ???
  }

  object Role {
    // A Reader has a read-only access to a single account
    case class Reader(accountId: AccountId, premiumUser: Boolean) extends Role
    // An Editor has edit access to a single account
    case class Editor(accountId: AccountId, favoriteFont: String) extends Role
    // An Admin has complete power over all accounts
    case object Admin extends Role
  }

  case class AccountId(value: Long)

  ///////////////////////
  // GO BACK TO SLIDES
  ///////////////////////

  ////////////////////////
  // 2. Variance
  ////////////////////////

  // 2a. Implement `parseRectangle` which parses a user input line (e.g. from the command line) into a `Rectangle`.
  // `parseRectangle` expects the following format 'R', space, Int, space, Int, end of line
  // such as parseRectangle("R 20 5") == Some(Rectangle(20, 5))
  // but     parseRectangle("C 20 5") == None
  //         parseRectangle("R 0")    == None
  // Note: `parseCircle` does something similar for `Circle`.
  // Note: `parseRectangle` returns an `InvariantOption` which is like a standard `Option` but with an invariant type parameter.
  def parseRectangle(inputLine: String): InvariantOption[Rectangle] =
    ???

  // 2b. Implement `parseShape` which parses a user input line (e.g. from the command line) into a `Shape`.
  // Try to reuse `parseCircle` and `parseRectangle`.
  // Note: You may need to `map` the `InvariantOption`.
  def parseShape(inputLine: String): InvariantOption[Shape] =
    ???

  sealed trait Shape
  object Shape {
    case class Circle(radius: Int)                extends Shape
    case class Rectangle(width: Int, height: Int) extends Shape
  }

  def parseCircle(inputLine: String): InvariantOption[Circle] =
    inputLine.split(" ").toList match {
      case "C" :: IntParser(radius) :: Nil => InvariantOption.Some(Circle(radius))
      case _                               => InvariantOption.None()
    }

  object IntParser {
    def unapply(s: String): Option[Int] =
      Try(s.toInt).toOption
  }

  ///////////////////
  // 3. Advanced API
  ///////////////////

  // 3a. Implement `filterDigits` which only keeps the digits from the list
  // such as filterDigits(List('a', '1', 'b', 'c', '4')) == List(1, 4)
  // Note: use `charToDigit`.
  def filterDigits(xs: List[Char]): List[Int] = ???

  def charToDigit(c: Char): Option[Int] =
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

  // 3b. Implement `checkAllDigits` which verifies that all input characters are digits
  // such as checkAllDigits(List('1', '2', '3')) == Some(List(1, 2, 3))
  // but     checkAllDigits(List('a', '1', 'b', 'c', '4')) == None
  // Note: you may want to use listSequence or listTraverse defined below.
  def checkAllDigits(orders: List[Char]): Option[List[Int]] = ???

  def listSequence[A](xs: List[Option[A]]): Option[List[A]] =
    xs.foldRight(Option(List.empty[A]))(
      (a: Option[A], acc: Option[List[A]]) => map2(a, acc)(_ :: _)
    )

  def map2[A, B, C](fa: Option[A], fb: Option[B])(f: (A, B) => C): Option[C] =
    (fa, fb) match {
      case (Some(a), Some(b)) => Some(f(a, b))
      case (None, _)          => None
      case (_, None)          => None
    }

  def listTraverse[A, B](xs: List[A])(f: A => Option[B]): Option[List[B]] =
    listSequence(xs.map(f))

  ///////////////////
  // 4. Limitation
  ///////////////////

  // 4a. Implement `sendUserEmail` which attempts to send an email to a user.
  // If the user is missing from the db, we will retry in 100 millis,
  // but if a user exists and it doesn't have an email address, then we fail the IO.
  // Can you reuse `getUserEmail`? Why?
  def sendUserEmail(db: DbApi, emailClient: EmailClient)(userId: UserId, emailBody: String): IO[Unit] =
    ???

  trait DbApi {
    def getAllUsers: IO[Map[UserId, User]]
  }

  trait EmailClient {
    def sendEmail(email: Email, body: String): IO[Unit]
  }

  // 4b. Implement `parsingJsonMessage` which attempts to parse a `String` into `Json` and returns either
  // a successful message in case of success (e.g. "OK") or
  // a descriptive error message in case of a failure (e.g. "invalid syntax at line 3: `foo :-: 4`'").
  // Note: assume you can only use `parseJson`.
  def parsingJsonMessage(jsonStr: String): String =
    ???

  def parseJson(jsonStr: String): Option[Json] =
    parser.parse(jsonStr).toOption
}
