package exercises.action.imperative

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDate}

import scala.io.StdIn
import scala.util.{Failure, Success, Try}

// Run the App using the green arrow next to object (if using IntelliJ)
// or run `sbt` in the terminal to open it in shell mode, then type:
// exercises/runMain exercises.actions.imperative.UserCreationApp
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
  // If the user enters something else, `readSubscribeToMailingList` throws an exception.
  // For example,
  // [Prompt] "Would you like to subscribe to our mailing list? [Y/N]"
  // [User] N
  // Returns false. But,
  // [Prompt] "Would you like to subscribe to our mailing list? [Y/N]"
  // [User] Nope
  // Throws an exception.
  // Note: You can read from the command line using `StdIn.readLine()`.
  // Note: You can use `throw new IllegalArgumentException("...")` to throw an exception.
  def readSubscribeToMailingList(): Boolean =
    ???

  // 2. How can we test `readSubscribeToMailingList`? 
  // We cannot use example-based tests or property-based tests 
  // because `readSubscribeToMailingList` depends on the
  // standard input `StdIn`.
  // Implement a new version of `readSubscribeToMailingList` which uses an instance
  // of `Console` to read/write lines.
  // Then, try to test this version using property-based testing.
  // Note: Check the `Console` companion object.
  def readSubscribeToMailingList(console: Console): Boolean =
    ???

  // 3. Implement `readDateOfBirth` which asks the date of birth of the user.
  // User must answer using the format `dd-mm-yyyy`, e.g. "18-03-2001" for 18th of March 2001.
  // If they enter an invalid response, `readDateOfBirth` throws an exception.
  // For example,
  // [Prompt] What's your date of birth? [dd-mm-yyyy]
  // [User] 21-07-1986
  // Returns LocalDate.of(1986,7,21). But,
  // [Prompt] What's your date of birth? [dd-mm-yyyy]
  // [User] 1986/07/21
  // Throws an exception.
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
  // Note: You will need to add a new Boolean field to `User`: `subscribedToMailingList`.
  // Note: How can you mock the current time? Check the `Clock` class in this package
  //       and update the signature of `readUser`.
  def readUser(console: Console): User =
    ???

}
