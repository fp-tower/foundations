package answers.errorhandling.either

import answers.errorhandling.either.EitherAnswers1.UserEmailError.{EmailNotFound, UserNotFound}
import answers.errorhandling.option.OptionAnswers.{Email, User, UserId}

import scala.util.control.NoStackTrace

object EitherAnswers1 {

  def getUserEmail(id: UserId, users: Map[UserId, User]): Either[UserEmailError, Email] =
    for {
      user  <- users.get(id).toRight(UserNotFound(id))
      email <- user.email.toRight(EmailNotFound(id))
    } yield email

  sealed abstract class UserEmailError(message: String) extends NoStackTrace {
    override def getMessage: String = message
  }
  object UserEmailError {
    case class UserNotFound(userId: UserId)  extends UserEmailError(s"User $userId is missing")
    case class EmailNotFound(userId: UserId) extends UserEmailError(s"User $userId has no email")
  }
}
