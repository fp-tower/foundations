package exercises.action.imperative

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import exercises.action.imperative.UserCreationExercises.User

import scala.util.Try

// Run the App using the green arrow next to object (if using IntelliJ)
// or run `sbt` in the terminal to open it in shell mode, then type:
// exercises/runMain exercises.actions.imperative.UserCreationServiceApp
object UserCreationServiceApp extends App {
  // initialise dependencies
  val console = Console.system
  val clock   = Clock.system
  val service = new UserCreationService(console, clock)

  // execute program
  service.readUser()
}

// Methods from `UserCreationExercises` repackaged in a class.
// `Console` and `Clock` dependencies are defined at the class level
// so that `readXXX` methods don't need to pass them around.
class UserCreationService(console: Console, clock: Clock) {
  import UserCreationService._

  def readName(): String = {
    console.writeLine("What's your name?")
    console.readLine()
  }

  def readDateOfBirth(): LocalDate =
    onError(
      action = {
        console.writeLine("What's your date of birth? [dd-mm-yyyy]")
        parseDate(console.readLine())
      },
      callback = _ => console.writeLine("""Incorrect format, for example enter "18-03-2001" for 18th of March 2001""")
    )

  // 1. Implement `readSubscribeToMailingList` using a similar approach to `readDateOfBirth`.
  // Note: Don't hesitate to move static helper methods to a `UserCreationService` companion
  //       object such as `parseYesNo`.
  def readSubscribeToMailingList(): Boolean =
    ???

  // 2. Implement `readUser` so that it reuses:
  // * `readName`
  // * `readDateOfBirth`
  // * `readSubscribeToMailingList`
  // `readUser` should allow the user to make up to 3 mistakes when they
  // attempt to enter the date of birth or subscription flag.
  def readUser(): User =
    ???

  // 3. add the methods `readDate` and `readYesNo` to `Console`

}

object UserCreationService {
  val dateOfBirthFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd-MM-uuuu")

  def parseDate(line: String): LocalDate =
    Try(LocalDate.parse(line, dateOfBirthFormatter))
      .getOrElse(
        throw new IllegalArgumentException(s"Expected a date with format dd-mm-yyyy but received $line")
      )

  def formatDate(date: LocalDate): String =
    dateOfBirthFormatter.format(date)
}
