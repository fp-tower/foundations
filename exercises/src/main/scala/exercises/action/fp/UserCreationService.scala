package exercises.action.fp

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import scala.util.{Failure, Success, Try}

// Run the App using the green arrow next to object (if using IntelliJ)
// or run `sbt` in the terminal to open it in shell mode then type:
// exercises/runMain exercises.actions.fp.UserCreationServiceApp
object UserCreationServiceApp extends App {
  val console = Console.system
  val clock   = Clock.system
  val service = new UserCreationService(console, clock)

  service.readUser.unsafeRun()
}

class UserCreationService(console: Console, clock: Clock) {
  import UserCreationService._
  import console._

  // 1. `readName` works as we expect (see exercises.action.fp.UserCreationServiceTest),
  // but `IO` makes the code more difficult to read by requiring:
  // * to wrap the entire method in `IO { }`
  // * to call `unsafeRun` on each internal action
  //
  // Let's capture the pattern of doing two actions one after the other using
  // the method `andThen` on the `IO` trait.
  // Then, we'll refactor `readName` with `andThen`.
  val readName: IO[String] =
    IO {
      console.writeLine("What's your name?").unsafeRun()
      console.readLine.unsafeRun()
    }

  // 2. `readDateOfBirth` is very complex for two reasons:
  // a) noisy error-handling logic in case the input is invalid.
  //    Let's capture this pattern using the method `onError` on the `IO` trait.
  // b) 3 internal actions are executed one after the other.
  //    Let's try to use `andThen`, if it doesn't work, investigate the
  //    methods `map` and `flatMap` on the IO trait.
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

  // 3. Refactor `readSubscribeToMailingList` using the same techniques as `readDateOfBirth`
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

  // 4. Refactor `readUser` using a for comprehension.
  // Then, update the logic so that users have up to 3 attempts to answer
  // the date of birth and mailing list question.
  // Note: Enable the last test in `UserCreationServiceTest`.
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

  //////////////////////////////////////////////
  // Bonus question (not covered by the video)
  //////////////////////////////////////////////

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
