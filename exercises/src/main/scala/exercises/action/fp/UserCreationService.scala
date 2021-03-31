package exercises.action.fp

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object UserCreationServiceApp extends App {
  val console = Console.system
  val clock   = Clock.system
  val service = new UserCreationService(console, clock)

  service.readName.unsafeRun()
}

class UserCreationService(console: Console, clock: Clock) {
  import UserCreationService._

  // 1. `readName` works as expected (see UserCreationServiceTest) but it is really ugly.
  // Refactor `readName` using the method `andThen` from `IO`
  val readName: IO[String] =
    IO {
      console.writeLine("What's your name?").unsafeRun()
      console.readLine.unsafeRun()
    }

  val readDateOfBirth: IO[LocalDate] =
    IO {
      console.writeLine("What's your date of birth? [dd-mm-yyyy]").unsafeRun()
      val line = console.readLine.unsafeRun()
      parseDateOfBirth(line).unsafeRun()
    }

  val readSubscribeToMailingList: IO[Boolean] =
    IO {
      console.writeLine("Would you like to subscribe to our mailing list? [Y/N]").unsafeRun()
      val line = console.readLine.unsafeRun()
      parseLineToBoolean(line).unsafeRun()
    }

  lazy val readUser: IO[User] =
    ???
}

object UserCreationService {
  val dateOfBirthFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd-MM-uuuu")

  def formatDateOfBirth(date: LocalDate): String =
    dateOfBirthFormatter.format(date)

  def parseDateOfBirth(line: String): IO[LocalDate] =
    IO { LocalDate.parse(line, dateOfBirthFormatter) }

  def formatYesNo(bool: Boolean): String =
    if (bool) "Y" else "N"

  def parseLineToBoolean(line: String): IO[Boolean] =
    IO {
      line match {
        case "Y" => true
        case "N" => false
        case _   => throw new IllegalArgumentException("Invalid input, expected Y/N")
      }
    }
}
