package answers.errorhandling

object OptionAnswers {

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
}
