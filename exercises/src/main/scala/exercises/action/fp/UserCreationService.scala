package exercises.action.fp

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import scala.util.{Failure, Success, Try}

object UserCreationServiceApp extends App {
  val console = Console.system
  val clock   = Clock.system
  val service = new UserCreationService(console, clock)

  service.readName.unsafeRun()
}

class UserCreationService(console: Console, clock: Clock) {
  import UserCreationService._

  // 1. `readName` works as we expect (see exercises.action.fp.UserCreationServiceTest),
  // but `IO` doesn't help, it only adds noise by requiring to:
  // * wrap the entire method in `IO { }`
  // * call `unsafeRun` on each internal action
  //
  // Let's capture the pattern of `readName` with the method `andThen` on the `IO` trait.
  // Then, when `andThen` is implemented, refactor `readName` with it.
  val readName: IO[String] =
    IO {
      console.writeLine("What's your name?").unsafeRun()
      console.readLine.unsafeRun()
    }

  // 2. `readDateOfBirth` is very difficult to read. Here are few issues:
  // a) Try/pattern-match/rethrow to print a message in case of a failure.
  //    --> capture this pattern with the method `onError` on the `IO` trait.
  // b) 3 internal actions are executed one after another
  val readDateOfBirth: IO[LocalDate] =
    IO {
      console.writeLine("What's your date of birth? [dd-mm-yyyy]").unsafeRun()
      val line = console.readLine.unsafeRun()
      Try(parseDateOfBirth(line).unsafeRun()) match {
        case Success(date) => date
        case Failure(exception) =>
          console.writeLine("""Incorrect format, for example enter "18-03-2001" for 18th of March 2001""").unsafeRun()
          throw exception
      }
    }

  val readSubscribeToMailingList: IO[Boolean] =
    IO {
      console.writeLine("Would you like to subscribe to our mailing list? [Y/N]").unsafeRun()
      val line = console.readLine.unsafeRun()
      Try(parseLineToBoolean(line).unsafeRun()) match {
        case Success(bool) => bool
        case Failure(exception) =>
          console.writeLine("""Incorrect format, enter "Y" for Yes or "N" for "No"""").unsafeRun()
          throw exception
      }
    }

  val readUser: IO[User] =
    IO {
      val name        = readName.unsafeRun()
      val dateOfBirth = readDateOfBirth.unsafeRun()
      val subscribed  = readSubscribeToMailingList.unsafeRun()
      val now         = clock.now.unsafeRun()
      val user        = User(name, dateOfBirth, subscribed, now)
      console.writeLine(s"User is $user").unsafeRun()
      user
    }
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
