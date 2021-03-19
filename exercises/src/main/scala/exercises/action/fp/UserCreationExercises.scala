package exercises.action.fp

import java.time.{Instant, LocalDate}
import java.time.format.DateTimeFormatter

object UserCreationExercises {

  // 1. `readName` works as expected but this implementation is far from satisfactory.
  // Refactor `readName` using the method `andThen` from `Action`
  def readName(console: Console): Action[String] =
    Action {
      console.writeLine("What's your name?").execute()
      console.readLine().execute()
    }

  def readDateOfBirth(console: Console): Action[LocalDate] =
    ???

  def readSubscribeToMailingList(console: Console): Action[Boolean] =
    ???

  val dateOfBirthFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd-MM-uuuu")

  case class User(
    name: String,
    dateOfBirth: LocalDate,
    subscribedToMailingList: Boolean,
    createdAt: Instant
  )

  def readUser(console: Console, clock: Clock): Action[User] =
    ???

}
