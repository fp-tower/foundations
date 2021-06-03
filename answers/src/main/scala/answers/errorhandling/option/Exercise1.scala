package answers.errorhandling.option

object Exercise1 {

  case class User(id: UserId, name: String, email: Option[Email])
  case class UserId(value: Long)
  case class Email(value: String)

  def getUserEmail(id: UserId, users: Map[UserId, User]): Option[Email] =
    for {
      user  <- users.get(id)
      email <- user.email
    } yield email

}
