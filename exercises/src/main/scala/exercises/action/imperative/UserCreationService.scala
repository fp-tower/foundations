package exercises.action.imperative

import java.time.LocalDate

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
// `Console` and `Clock` dependencies are passed at the class level
// so that `readXXX` methods don't need to pass them around.
class UserCreationService(console: Console, clock: Clock) {

  def readUser(): User = {
    val name        = readName()
    val dateOfBirth = retry(3)(() => readDateOfBirth())
    val subscribed  = retry(3)(() => readSubscribeToMailingList())
    val now         = clock.now()
    ???
  }

  def readName(): String = {
    console.writeLine("What's your name?")
    console.readLine()
  }

  def readDateOfBirth(): LocalDate =
    ???

  def readSubscribeToMailingList(): Boolean =
    ???

}
