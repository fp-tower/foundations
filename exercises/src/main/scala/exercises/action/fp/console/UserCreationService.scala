package exercises.action.fp.console

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import exercises.action.fp.IO

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

  // 1. `readName` works as we expect, but `IO` makes the code
  // more difficult to read by requiring:
  // * to wrap the entire method in `IO { }`
  // * to call `unsafeRun` on each internal action
  //
  // Let's capture the pattern of doing two actions, one after the other, using
  // the method `andThen` on the `IO` trait.
  // Then, we'll refactor `readName` with `andThen`.
  // Note: You can find tests in `exercises.action.fp.console.UserCreationServiceTest`
  val readName: IO[String] =
    IO {
      console.writeLine("What's your name?").unsafeRun()
      console.readLine.unsafeRun()
    }

  // 2. Refactor `readDateOfBirth` so that the code combines the three internal `IO`
  // instead of executing each `IO` one after another using `unsafeRun`.
  // For example, try to use `andThen`.
  // If it doesn't work investigate the methods `map` and `flatMap` on the `IO` trait.
  val readDateOfBirth: IO[LocalDate] =
    IO {
      console.writeLine("What's your date of birth? [dd-mm-yyyy]").unsafeRun()
      val line = console.readLine.unsafeRun()
      parseDateOfBirth(line).unsafeRun()
    }

  // 3. Refactor `readSubscribeToMailingList` and `readUser` using the same techniques as `readDateOfBirth`.
  val readSubscribeToMailingList: IO[Boolean] =
    IO {
      console.writeLine("Would you like to subscribe to our mailing list? [Y/N]").unsafeRun()
      val line = console.readLine.unsafeRun()
      parseLineToBoolean(line).unsafeRun()
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

  //////////////////////////////////////////////
  // PART 2: For Comprehension
  //////////////////////////////////////////////

  // 4. Refactor `readDateOfBirth` using a for comprehension.

  // 5. Refactor `readSubscribeToMailingList` and `readUser` using a for comprehension.

  //////////////////////////////////////////////
  // PART 3: Error handling
  //////////////////////////////////////////////

  // 6. Refactor `readDateOfBirth` so that it prints the following error message
  // when a user enters an invalid input:
  // Incorrect format, for example enter "18-03-2001" for 18th of March 2001
  // Use the method `onError` on `IO` to implement the error handling logic.
  // Note: Uncomment the last line of `readDate failure` test in `UserCreationServiceTest`.

  // 7. Refactor `readSubscribeToMailingList` so that it prints the following error message
  // when a user enters an invalid input:
  // Incorrect format, enter "Y" for Yes or "N" for "No"
  // Use the method `onError` on `IO` to implement the error handling logic.
  // Note: Uncomment the last line of `readSubscribeToMailingList failure` test in `UserCreationServiceTest`.

  // 8. Refactor `readUser` so that users have up to 3 attempts to answer
  // the date of birth and mailing list questions.
  // Use the method `retry` on `IO`.
  // Note: Enable the final test in `UserCreationServiceTest`.

  //////////////////////////////////////////////
  // PART 4: IO clean-up
  //////////////////////////////////////////////

  // 9. Refactor `andThen` method on `IO` so that it doesn't use `unsafeRun`.

  // 10. `map` and `flatMap` on `IO` do almost the same thing.
  // Could you implement one using the other? If yes, which one?

  // 11. Implement `attempt` on `IO`.

  // 12. Refactor `onError` on `IO` so that that it doesn't use `unsafeRun`.
  // You will likely need to use a combination of `flatMap` and `attempt`.

  // 13. Refactor `retry` on `IO` so that that it doesn't use `unsafeRun`.
  // You will likely need to use a combination of `flatMap` and `attempt`.

  // 14. Implement the method `handleErrorWith` on `IO`.

  // 15. Simplify the code of `onError` and `retry` using `handleErrorWith`

  //////////////////////////////////////////////
  // Bonus question (not covered by the videos)
  //////////////////////////////////////////////

  // 16. `onError` takes a `cleanup` function which returns an IO.
  // This means we could end up with two exceptions:
  // * One from the current IO
  // * One from `cleanup`
  // For example, if both `emailClient.send` and `db.saveUnsentEmail` fail
  // emailClient.send(email).onError(exception =>
  //   db.saveUnsentEmail(email, exception.getMessage)
  // )
  // In this case, we would like `onError` to swallow the error from `cleanup` and
  // rethrow the error from the current IO.
  // Add a test case for this scenario and update `onError` implementation.

  // 17. Write a property-based test for `retry` which covers both:
  // a) successes, when `maxAttempt >  number of errors`
  // b) failures, when `maxAttempt <= number of errors`

  // 18. The implementation of `retry` used in the video has a bug.
  // Were you able to identify it?
  // Try to write a test which exhibits the issue.

  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //                                          //
  //                SPOILER                   //
  //                                          //
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////

  // `retry` is a recursive function but it is not tail recursive.
  // If you add a @tailrec annotation to `retry`, the program doesn't compile.
  // One way to check if `retry` is stack-safe is to write a test where
  // `maxAttempt` and `number of errors` are high (around 10 000).
  //
  // There are two ways to make `retry` stack-safe:
  // a) rewrite it using a while loop, without recursion.
  // b) wait until the end of the chapter.

}

object UserCreationService {
  val dateOfBirthFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd-MM-uuuu")

  def formatDateOfBirth(date: LocalDate): String =
    dateOfBirthFormatter.format(date)

  def parseDateOfBirth(line: String): IO[LocalDate] =
    IO(LocalDate.parse(line, dateOfBirthFormatter))

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
