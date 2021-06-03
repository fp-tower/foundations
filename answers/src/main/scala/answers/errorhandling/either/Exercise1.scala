package answers.errorhandling.either

import answers.errorhandling.either.Exercise1.UserEmailError.{EmailNotFound, UserNotFound}

object Exercise1 {

  def getUserEmail(id: UserId, users: Map[UserId, User]): Either[UserEmailError, Email] =
    for {
      user  <- users.get(id).toRight(UserNotFound(id))
      email <- user.email.toRight(EmailNotFound(id))
    } yield email

  case class User(id: UserId, name: String, email: Option[Email])
  case class UserId(value: Long)
  case class Email(value: String)

  sealed trait UserEmailError
  object UserEmailError {
    case class UserNotFound(userId: UserId)  extends UserEmailError
    case class EmailNotFound(userId: UserId) extends UserEmailError
  }

}
