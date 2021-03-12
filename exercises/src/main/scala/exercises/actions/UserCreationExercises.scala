package exercises.actions

import java.time.{Instant, LocalDate}
import java.time.format.DateTimeFormatter

import scala.io.StdIn

// Run the App using the green arrow next to object (if using IntelliJ)
// or run `sbt` in the terminal to open it in shell mode then type:
// exercises/runMain exercises.actions.UserCreationApp
object UserCreationApp extends App {
  import UserCreationExercises._

  readUser()
}

object UserCreationExercises {
  val dateOfBirthFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

  case class User(name: String, dateOfBirth: LocalDate, createdAt: Instant)

  def readUser(): User = {
    println("What's your name?")
    val name = StdIn.readLine()
    println("What's your date of birth? [dd-mm-yyyy]")
    val dateOfBirth = LocalDate.parse(StdIn.readLine(), dateOfBirthFormatter)
    val now         = Instant.now()
    val user        = User(name, dateOfBirth, now)
    println(s"User is $user")
    user
  }

  // 1. Implement `readSubscribeToMailingList` which asks if the user wants to
  // subscribe to our mailing list. They can answer "Y" for yes or "N" for No.
  // If the user enters something else, we throw an exception.
  // For example,
  // [Prompt] "Would you like to subscribe to our mailing list? [Y/N]"
  // [User] N
  // Returns false
  // Note: You can use `throw new IllegalArgumentException("...")` to throw an exception
  def readSubscribeToMailingList(): Boolean =
    ???

  // 2. How can we test `readSubscribeToMailingList` or `readUser`? We cannot write
  // an example-based or property-based test because these functions depend on
  // the standard input `StdIn`.
  // Implement a new version of `readSubscribeToMailingList` which uses an instance
  // of the `Console` interface to read/write lines.
  // Then, try to test this version using example-based or property-based testing.
  // Note: Check the `Console` companion object.
  def readSubscribeToMailingList(console: Console): Boolean =
    ???

  // 3. Implement `readDateOfBirth` which asks the date of birth of the user.
  // They must answer with the format `dd-mm-yyyy`, e.g. "18-03-2001" for 18th of March 2001.
  // If the user enters something else, we throw an exception.
  // For example,
  // [Prompt] What's your date of birth? [dd-mm-yyyy]
  // [User] 21-07-1986
  // Returns LocalDate.of(1986,7,21)
  // Note: You can use `LocalDate.parse` to parse a String into a LocalDate.
  // Note: You can use the formatter `dateOfBirthFormatter`.
  def readDateOfBirth(console: Console): LocalDate =
    ???

  // 4. Implement a testable version of `readUser`.
  // For example,
  // [Prompt] "What's your name?"
  // [User] Eda
  // [Prompt] What's your date of birth? [dd-mm-yyyy]
  // [User] 18-03-2001
  // [Prompt] "Would you like to subscribe to our mailing list? [Y/N]"
  // [User] Y
  // Returns User(
  //   name = "Eda",
  //   dateOfBirth = LocalDate.of(2001, 3, 18),
  //   subscribedToMailingList = true,
  //   createdAt = Instant.now()
  // )
  // Note: You will need to add `subscribedToMailingList: Boolean field to `User`.
  // Note: How can you mock the current time? Check the `Clock` class in this package
  //       and update the signature of `readUser`.
  def readUser(console: Console): User =
    ???

  // 5. Implement `readSubscribeToMailingListRetry` which behaves like
  // `readSubscribeToMailingList` but retries in case the user enters an invalid input.
  // For example: readSubscribeToMailingListRetry(console, maxAttempt = 2)
  // [Prompt] "Would you like to subscribe to our mailing list? [Y/N]"
  // [User] No
  // [Prompt] Incorrect format, enter "Y" for Yes or "N" for "No"
  // [Prompt] "Would you like to subscribe to our mailing list? [Y/N]"
  // [User] N
  // Returns false
  // But, readSubscribeToMailingListRetry(console, maxAttempt = 1)
  // [Prompt] "Would you like to subscribe to our mailing list? [Y/N]"
  // [User] No
  // Throw an exception because the user had only 1 attempt and they entered an invalid input.
  // Note: Don't try to generalise the retry logic yet, we will do it later.
  def readSubscribeToMailingListRetry(console: Console, maxAttempt: Int): Boolean =
    ???

  // 6. Implement `readDateOfBirthRetry` which behaves like
  // `readDateOfBirth` but retries in case the user enters an invalid input.
  // For example: readDateOfBirth(dateOfBirthFormatter, maxAttempt = 2)
  // [Prompt] What's your date of birth? [dd-mm-yyyy]
  // [User] 21st of July
  // [Prompt] Incorrect format, for example enter "18-03-2001" for 18th of March 2001
  // [Prompt] What's your date of birth? [dd-mm-yyyy]
  // [User] 21-07-1986
  // Returns LocalDate.of(1986,7,21)
  // But, readDateOfBirth(dateOfBirthFormatter, maxAttempt = 1)
  // [Prompt] What's your date of birth? [dd-mm-yyyy]
  // [User] 21st of July
  // Throw an exception because the user had only 1 attempt and they entered an invalid input.
  // Note: Don't try to generalise the retry logic yet, we will do it later.
  def readDateOfBirthRetry(console: Console, maxAttempt: Int): LocalDate =
    ???

  // 4. Implement `retry`, a function that evaluate a block of code until it succeeds or
  // exhausts the number of retry.
  // If the code block succeeds, then `retry` returns the result.
  // If the code block throws an exception, then `retry` attempts to re-evaluate `block`.
  // If `maxAttempt` is lower than or equal to 0, then `retry` throws an exception.
  // For example,
  // var counter = 0
  // def exec(): String = {
  //   counter += 1
  //   if(counter < 3) throw new Exception("Boom!")
  //   else "Hello"
  // }
  // retry(maxAttempt = 5)(exec) == "Hello"
  // Returns "Hello" because `exec` fails twice and then succeeds when counter reaches 3.
  // retry(maxAttempt = 2){ () => throw new Exception("Boom!") }
  // Throws an exception because `block` always fails
  // Note: `block: () => A` is a val function which takes no argument.
  //       You can execute `block` using `block()`
  def retry[A](maxAttempt: Int)(block: () => A): A =
    ???

  // 5. Refactor `readDateOfBirth` and `readSubscribeToMailingList` to use `retry`.
  // Does it produce the same result?
  // If not, try to implement `retryWithError` which provides an `onError` callback.
  // `onError` should be called whenever `block` fails. For example,
  // `onError = error => println("Oops: " + error.getMessage)`
  def retryWithError[A](maxAttempt: Int)(block: => A, onError: Throwable => Any): A =
    ???

  //////////////////////////////////////////////
  // Bonus question (not covered by the video)
  //////////////////////////////////////////////

  // 6. Implement a testable version of `readUser` which uses both
  // the `Console` and `Clock` interface.
  def readUser(console: Console, clock: Clock): User =
    ???

}
