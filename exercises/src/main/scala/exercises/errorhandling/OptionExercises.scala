package exercises.errorhandling

object OptionExercises {

  // 1a. Implement `getUserEmail` that looks up the email of a user using its user id such as
  // val userMap = Map(
  //   222 -> User(222, "john" , "j@x.com"),
  //   123 -> User(123, "elisa", "e@y.com"),
  //   444 -> User(444, "bob")
  // )
  // getUserEmail(123, userMap) == Some("e@y.com")
  // getUserEmail(444, userMap) == None
  // getUserEmail(111, userMap) == None
  def getUserEmail(id: UserId, users: Map[UserId, User]): Option[Email] = ???

  case class User(id: UserId, name: String, email: Option[Email])
  case class UserId(value: Long)
  case class Email(value: String)

  sealed trait Role {
    import Role._

    // 1b. Implement `optSingleAccountId` which return the account id if the role is a Reader or Editor
    // such as Editor(123, "Comic Sans").optSingleAccountId == Some(123)
    //         Reader(123, premiumUser = true).optEditor == Some(123)
    // but     Admin.optEditor == None
    // Note: you can pattern match on Role using `this match { case Reader(...) => ... }`
    def optSingleAccountId: Option[AccountId] = ???

    // 1c. Implement `getEditor` which checks if the current `Role` is an `Editor`
    // such as Editor(123, "Comic Sans").optEditor == Some(Editor(123, "Comic Sans"))
    // but     Reader(123, premiumUser = true).optEditor == None
    def optEditor: Option[Role.Editor] = ???
  }
  object Role {
    // A Reader has read-only access on an account
    case class Reader(accountId: AccountId, premiumUser: Boolean) extends Role
    // An Editor can update resources on an account
    case class Editor(accountId: AccountId, favoriteFont: String) extends Role
    // An Admin has complete power over all accounts
    case object Admin extends Role
  }

  case class AccountId(value: Long)

  // 1d. Implement `filterDigits` which only keeps the digits from the list
  // such as filterDigits(List('a', '1', 'b', 'c', '4')) == List(1, 4)
  // Note: use `charToDigit`
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

  // 1e. Implement `checkAllDigits` which verifies all input characters are digits
  // such as checkAllDigits(List('a', '1', 'b', 'c', '4')) == None
  // but     checkAllDigits(List('1', '2', '3')) == Some(List(1, 2, 3))
  // Note: you may want to use listSequence or listTraverse defined below
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

}
