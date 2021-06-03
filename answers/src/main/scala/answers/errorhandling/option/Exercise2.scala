package answers.errorhandling.option

object Exercise2 {
  sealed trait Role {
    import Role._

    def getSingleAccountId: Option[AccountId] =
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

  case class AccountId(value: Long)
}
