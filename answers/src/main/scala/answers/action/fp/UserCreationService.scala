package answers.action.fp

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object UserCreationServiceApp extends App {
  val console = Console.system
  val clock   = Clock.system
  val service = new UserCreationService(console, clock)

  service.readUser.unsafeRun()
}

class UserCreationService(console: Console, clock: Clock) {
  import UserCreationService._

  val readName: IO[String] =
    for {
      _    <- console.writeLine("What's your name?")
      name <- console.readLine
    } yield name

  val readDateOfBirth: IO[LocalDate] = {
    val errorMessage = """Incorrect format, for example enter "18-03-2001" for 18th of March 2001"""

    for {
      _    <- console.writeLine("What's your date of birth? [dd-mm-yyyy]")
      date <- console.readDate(dateOfBirthFormatter).onError(_ => console.writeLine(errorMessage))
    } yield date
  }

  val readSubscribeToMailingList: IO[Boolean] = {
    val errorMessage = """Incorrect format, enter "Y" for Yes or "N" for "No""""

    for {
      _    <- console.writeLine("Would you like to subscribe to our mailing list? [Y/N]")
      bool <- console.readYesNo.onError(_ => console.writeLine(errorMessage))
    } yield bool
  }

  val readUser: IO[User] =
    for {
      name       <- readName
      dob        <- readDateOfBirth.retry(3)
      subscribed <- readSubscribeToMailingList.retry(3)
      now        <- clock.now
      user = User(name, dob, subscribed, now)
      _ <- console.writeLine(s"User is $user")
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
