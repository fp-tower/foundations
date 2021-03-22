package exercises.action.imperative

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import exercises.action.imperative.UserCreationExercises.User
import exercises.action.imperative.RetryExercises._

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

  // 1. Implement `readDateOfBirth`, you can copy your answer
  // from `UserCreationExercises`.
  // Note: `dateOfBirthFormatter` is already in scope because
  //       it is defined in the companion object of `UserCreationService`.
  //       You may want to define other helper methods there.
  def readDateOfBirth(): LocalDate =
    ???

  // 2. Implement `readSubscribeToMailingList`, you can copy your answer
  // from `UserCreationExercises`.
  // Note: `parseYesNo` is already in scope because
  def readSubscribeToMailingList(): Boolean =
    ???

  // 3. Implement `readUser` so that it reuses:
  // * `readName`
  // * `readDateOfBirth`
  // * `readSubscribeToMailingList`
  // `readUser` should allow user to make up to 3 mistakes when they
  // attempt to enter the date of birth or subscription flag.
  // Note: You can find a example-based test in `UserCreationExercisesTest`.
  def readUser(): User =
    ???

}

object UserCreationService {
  val dateOfBirthFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd-MM-uuuu")

  def parseYesNo(line: String): Boolean =
    line match {
      case "Y"   => true
      case "N"   => false
      case other => throw new IllegalArgumentException(s"""Expected "Y" or "N" but received $other""")
    }
}
