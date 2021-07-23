package exercises.errorhandling.option

import exercises.errorhandling.option.OptionExercises.Role._

object OptionExercises {

  sealed trait Role
  object Role {
    // A Reader has a read-only access to a single account
    case class Reader(accountId: AccountId, premiumUser: Boolean) extends Role
    // An Editor has edit access to a single account
    case class Editor(accountId: AccountId, favoriteFont: String) extends Role
    // An Admin has unlimited access on all accounts
    case object Admin extends Role
  }
  case class AccountId(value: Long)

  // 1. Implement `getAccountId` which returns the account id associated
  // with this role (`Reader` or `Editor`). `Admin` do not have an account id.
  // For example,
  // getAccountId(Reader(123, premiumUser = true)) == Some(123)
  // getAccountId(Editor(123, "Comic Sans")) == Some(123)
  // getAccountId(Admin) == None
  // Note: You can pattern match on `Role` using `role match { case Reader(...) => ... }`
  // Note: Once you have implemented `getAccountId`, try to move it
  //       inside the `Role` class.
  def getAccountId(role: Role): Option[AccountId] =
    ???

  case class User(id: UserId, name: String, role: Role, email: Option[Email])
  case class UserId(value: Long)
  case class Email(value: String)

  // 2. Implement `getUserEmail` which looks up the email address of a user.
  // The email may be missing if:
  // * the user doesn't exist, or
  // * the user exists but doesn't have an email address
  // For example,
  // val users = Map(
  //   222 -> User(222, "john" , Admin, Some("j@x.com")),
  //   123 -> User(123, "elisa", Admin, Some("e@y.com")),
  //   444 -> User(444, "bob"  , Admin, None)
  // )
  // getUserEmail(123, users) == Some("e@y.com")
  // getUserEmail(111, users) == None // no user
  // getUserEmail(444, users) == None // no email
  // Note: You can use the method `get` on a `Map` to lookup a value by key
  def getUserEmail(userId: UserId, users: Map[UserId, User]): Option[Email] =
    ???

  // 3. Implement `getAccountIds` which returns all the account ids associated
  // with the users. If a user has no account id (e.g. `Admin`), ignore them.
  // For example,
  // getAccountIds(List(
  //   User(111, "Eda", Editor(555, "Comic Sans"), Some("e@y.com")),
  //   User(222, "Bob", Reader(555, true)        , None),
  //   User(333, "Lea", Reader(741, false)       , None),
  //   User(444, "Jo" , Admin                    , Some("admin@fp-tower.com")),
  // ))
  // returns List(555, 741)
  // Note: In case two or more users have the same account id, `getAccountIds` only returns one.
  def getAccountIds(users: List[User]): List[AccountId] =
    ???

  // 4. Implement `checkAllEmails` which checks if all users have an email and returns them.
  // If one or more users don't have an email `checkAllEmails` returns false.
  // checkAllEmails(List(
  //   User(111, "Eda", Editor(555, "Comic Sans"), Some("e@y.com")),
  //   User(444, "Jo" , Admin                    , Some("admin@fp-tower.com")),
  // ))
  // returns Some(List("e@y.com", "admin@fp-tower.com"))
  // checkAllEmails(List(
  //   User(111, "Eda", Editor(555, "Comic Sans"), Some("e@y.com")),
  //   User(222, "Bob", Reader(555, true)        , None),
  //   User(333, "Lea", Reader(741, false)       , None),
  //   User(444, "Jo" , Admin                    , Some("admin@fp-tower.com")),
  // ))
  // returns None
  // Note: You may want to use `sequence` or `traverse` defined below.
  def checkAllEmails(users: List[User]): Option[List[Email]] =
    ???

  // 5. If all options are defined (`Some`), `sequence` extracts all the values in a List.
  // If one or more options are None, `sequence` returns None.
  // sequence(List(Some(1), Some(2), Some(3))) == Some(List(1, 2, 3))
  // sequence(List(Some(1), None   , Some(3))) == None
  def sequence[A](options: List[Option[A]]): Option[List[A]] =
    ???

  // Alias for `map` followed by `sequence`
  def traverse[A, B](values: List[A])(transform: A => Option[B]): Option[List[B]] =
    sequence(values.map(transform))

  //////////////////////////////////////////////
  // Bonus question (not covered by the videos)
  //////////////////////////////////////////////

  // 6. Implement `asEditor` which checks if the current `Role` is an `Editor`.
  // For example,
  // asEditor(Editor(123, "Comic Sans")) == Some(Editor(123, "Comic Sans"))
  // asEditor(Reader(123, premiumUser = true)) == None
  // asEditor(Admin) == None
  // Note: Once you have implemented `getAccountId`, try to move it
  //       inside the `Role` class.
  def asEditor(role: Role): Option[Editor] =
    ???
}
