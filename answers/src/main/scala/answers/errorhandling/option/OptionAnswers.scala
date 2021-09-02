package answers.errorhandling.option

object OptionAnswers {

  sealed trait Role {
    import Role._

    def getAccountId: Option[AccountId] =
      this match {
        case x: Reader => Some(x.accountId)
        case x: Editor => Some(x.accountId)
        case Admin     => None
      }

    def asEditor: Option[Role.Editor] =
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

  case class User(id: UserId, name: String, role: Role, email: Option[Email])
  case class AccountId(value: Long)
  case class UserId(value: Long)
  case class Email(value: String)

  def getUserEmail(userId: UserId, users: Map[UserId, User]): Option[Email] =
    for {
      user  <- users.get(userId)
      email <- user.email
    } yield email

  def getAccountIds(users: List[User]): List[AccountId] =
    users
      .flatMap(_.role.getAccountId)
      .distinct
      .sortBy(_.value)

  def checkAllEmails(users: List[User]): Option[List[Email]] =
    users.traverse(_.email)

  def sequence[A](options: List[Option[A]]): Option[List[A]] =
    options
      .foldLeft(Option(List.empty[A])) { (state, option) =>
        state.zip(option).map { case (list, value) => value :: list }
      }
      .map(_.reverse)

  def traverse[A, B](values: List[A])(transform: A => Option[B]): Option[List[B]] =
    sequence(values.map(transform))

}
