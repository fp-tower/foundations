package answers.errorhandling

import answers.errorhandling.OptionAnswers.Shape.{Circle, Rectangle}
import answers.sideeffect.IOAnswers.IO
import answers.sideeffect.IOAsync
import exercises.errorhandling.InvariantOption

import scala.concurrent.duration._
import scala.util.Try

object OptionAnswers {

  ////////////////////////
  // 1. Use cases
  ////////////////////////

  def getUserEmail(id: UserId, users: Map[UserId, User]): Option[Email] =
    for {
      user  <- users.get(id)
      email <- user.email
    } yield email

  case class User(id: UserId, name: String, email: Option[Email])
  case class UserId(value: Long)
  case class Email(value: String)

  sealed trait Role {
    import Role._

    def optSingleAccountId: Option[AccountId] =
      this match {
        case x: Reader => Some(x.accountId)
        case x: Editor => Some(x.accountId)
        case Admin     => None
      }

    def optEditor: Option[Role.Editor] =
      this match {
        case x: Editor         => Some(x)
        case _: Reader | Admin => None
      }
  }
  object Role {
    case class Reader(accountId: AccountId, premiumUser: Boolean) extends Role
    case class Editor(accountId: AccountId, favoriteFont: String) extends Role
    case object Admin                                             extends Role
  }

  case class AccountId(value: Long)

  ////////////////////////
  // 2. Variance
  ////////////////////////

  def parseShape(inputLine: String): InvariantOption[Shape] =
    widen[Shape.Circle, Shape](parseCircle(inputLine)).orElse(
      widen[Shape.Rectangle, Shape](parseRectangle(inputLine))
    )

  def widen[A, B >: A](fa: InvariantOption[A]): InvariantOption[B] =
    fa.map(a => a: B)

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

  def parseRectangle(inputLine: String): InvariantOption[Rectangle] =
    inputLine.split(" ").toList match {
      case "R" :: IntParser(width) :: IntParser(height) :: Nil => InvariantOption.Some(Rectangle(width, height))
      case _                                                   => InvariantOption.None()
    }

  object IntParser {
    def unapply(s: String): Option[Int] =
      Try(s.toInt).toOption
  }

  def filterDigits(xs: List[Char]): List[Int] =
    xs.flatMap(charToDigit(_).toList)

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

  def checkAllDigits(orders: List[Char]): Option[List[Int]] =
    listTraverse(orders)(charToDigit)

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

  def sendUserEmail(db: DbApi, emailClient: EmailClient)(userId: UserId, emailBody: String): IOAsync[Unit] =
    db.getAllUsers.flatMap { users =>
      users.get(userId) match {
        case None => IOAsync.sleep(100.millis) *> sendUserEmail(db, emailClient)(userId, emailBody)
        case Some(user) =>
          user.email match {
            case None        => IOAsync.fail(new Exception(s"User $userId doesn't have an email"))
            case Some(email) => emailClient.sendEmail(email, emailBody)
          }
      }
    }

  trait DbApi {
    def getAllUsers: IOAsync[Map[UserId, User]]
  }

  trait EmailClient {
    def sendEmail(email: Email, body: String): IOAsync[Unit]
  }
}
