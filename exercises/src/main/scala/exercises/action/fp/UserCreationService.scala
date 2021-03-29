package exercises.action.fp

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object UserCreationServiceApp extends App {
  val console = Console.system
  val clock   = Clock.system
  val service = new UserCreationService(console, clock)

  service.readName.execute()
}

class UserCreationService(console: Console, clock: Clock) {
  import UserCreationService._

  // 1. `readName` works as expected but this implementation is far from satisfactory.
  // Refactor `readName` using the method `andThen` from `Action`
  val readName: Action[String] =
    Action {
      console.writeLine("What's your name?").execute()
      console.readLine.execute()
    }

  val readDateOfBirth: Action[LocalDate] =
    Action {
      console.writeLine("What's your date of birth? [dd-mm-yyyy]").execute()
      val line = console.readLine.execute()
      LocalDate.parse(line, dateOfBirthFormatter)
    }

  val readSubscribeToMailingList: Action[Boolean] =
    Action {
      console.writeLine("Would you like to subscribe to our mailing list? [Y/N]").execute()
      val line = console.readLine.execute()
      parseLineToBoolean(line).execute()
    }

  lazy val readUser: Action[User] =
    ???
}

object UserCreationService {
  val dateOfBirthFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd-MM-uuuu")

  def formatDateOfBirth(date: LocalDate): String =
    dateOfBirthFormatter.format(date)

  def formatYesNo(bool: Boolean): String =
    if (bool) "Y" else "N"

  def parseLineToBoolean(line: String): Action[Boolean] =
    Action {
      line match {
        case "Y" => true
        case "N" => false
        case _   => throw new IllegalArgumentException("Invalid input, expected Y/N")
      }
    }
}
