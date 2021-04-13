package answers.action.fp.console

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import answers.action.fp.IO

object UserCreationServiceApp extends App {
  val console = Console.system
  val clock   = Clock.system
  val service = new UserCreationService(console, clock)

  service.readUser.unsafeRun()
}

class UserCreationService(console: Console, clock: Clock) {
  import UserCreationService._
  import console._

  val readName: IO[String] =
    for {
      _    <- writeLine("What's your name?")
      name <- readLine
    } yield name

  val readDateOfBirth: IO[LocalDate] = {
    val printError = writeLine("""Incorrect format, for example enter "18-03-2001" for 18th of March 2001""")

    for {
      _    <- writeLine("What's your date of birth? [dd-mm-yyyy]")
      date <- readDate(dateOfBirthFormatter).onError(_ => printError)
    } yield date
  }

  val readSubscribeToMailingList: IO[Boolean] = {
    val printError = writeLine("""Incorrect format, enter "Y" for Yes or "N" for "No"""")

    for {
      _    <- writeLine("Would you like to subscribe to our mailing list? [Y/N]")
      bool <- readYesNo.onError(_ => printError)
    } yield bool
  }

  val readUser: IO[User] =
    for {
      name        <- readName
      dateOfBirth <- readDateOfBirth.retry(3)
      subscribed  <- readSubscribeToMailingList.retry(3)
      now         <- clock.now
      user = User(name, dateOfBirth, subscribed, now)
      _ <- writeLine(s"User is $user")
    } yield user
}

object UserCreationService {
  val dateOfBirthFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd-MM-uuuu")

  def formatDateOfBirth(date: LocalDate): String =
    dateOfBirthFormatter.format(date)

  def formatYesNo(bool: Boolean): String =
    if (bool) "Y" else "N"
}
