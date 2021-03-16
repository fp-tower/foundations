package answers.action.fp

import java.time.{Instant, LocalDate}
import java.time.format.DateTimeFormatter

object UserCreationAnswersApp extends App {
  import UserCreationAnswers._

  readUser(Console.system, Clock.system).execute()
}

object UserCreationAnswers {
  val dateOfBirthFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd-MM-uuuu")

  case class User(
    name: String,
    dateOfBirth: LocalDate,
    subscribedToMailingList: Boolean,
    createdAt: Instant
  )

  def readUser(console: Console, clock: Clock): Action[User] =
    for {
      name       <- readName(console)
      dob        <- readDateOfBirth(console).retry(3)
      subscribed <- readSubscribeToMailingList(console).retry(3)
      now        <- clock.now
      user = User(name, dob, subscribed, now)
      _ <- console.writeLine(s"User is $user")
    } yield user

  def readName(console: Console): Action[String] =
    for {
      _    <- console.writeLine("What's your name?")
      name <- console.readLine()
    } yield name

  def readDateOfBirth(console: Console): Action[LocalDate] = {
    val errorMessage = """Incorrect format, for example enter "18-03-2001" for 18th of March 2001"""

    for {
      _    <- console.writeLine("What's your date of birth? [dd-mm-yyyy]")
      date <- console.readDate(dateOfBirthFormatter).onError(_ => console.writeLine(errorMessage))
    } yield date
  }

  def readSubscribeToMailingList(console: Console): Action[Boolean] = {
    val errorMessage = """Incorrect format, enter "Y" for Yes or "N" for "No""""

    for {
      _    <- console.writeLine("Would you like to subscribe to our mailing list? [Y/N]")
      bool <- console.readBoolean.onError(_ => console.writeLine(errorMessage))
    } yield bool
  }

}
