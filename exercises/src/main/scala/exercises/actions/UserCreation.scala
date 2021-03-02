package exercises.actions

import java.time.{Instant, LocalDate}
import java.time.format.DateTimeFormatter

import scala.io.StdIn

// Run the App using the green arrow next to object (if using IntelliJ)
// or run `sbt` in your terminal to open it in shell mode then type:
// exercises/runMain exercises.actions.UserCreationApp
object UserCreationApp extends App {
  import UserCreation._

  readUser()
}

object UserCreation {

  case class User(name: String, dateOfBirth: LocalDate, createdAt: Instant)

  def readUser(): User = {
    println("What's your name?")
    val name = StdIn.readLine()
    println("What's your date of birth (dd-mm-yyyy)?")
    val formatter   = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val dateOfBirth = LocalDate.parse(StdIn.readLine(), formatter)
    val now         = Instant.now()
    val user        = User(name, dateOfBirth, now)
    println(s"User is $user")
    user
  }

  // 1. When `readUser` requests the date of birth from the user, it might fail
  // if the input doesn't match the expected format.
  // Add some retry logic to `readUser` so that it attempts to read the date of birth up to 3 times.
  // For example:
  // [Prompt] What's your date of birth (dd-mm-yyyy)?
  // [User] 21st of July
  // [Prompt] "21st of July" is not a valid date
  // [Prompt] What's your date of birth (dd-mm-yyyy)?
  // [User] 21-07-1986
  // Success! But if the user enters an incorrect input 3 times,
  // the function will throw an exception. Same as before.
  // Note: Don't try to generalise the retry logic yet, we will do it in 3).

  // 2. Add a Boolean field `subscribedToMailingList` to `User`.
  // Update `readUser` so that it asks an additional question to fill this value.
  // Users can answer with "Y" for true or "N" for false. Anything else is a failure.
  // Similarly to date of birth, allow the user to retry up 3 times.
  //
  // For example:
  // [Prompt] What's your date of birth (dd-mm-yyyy)?
  // [User] 21-07-1986
  // [Prompt] Would you like to subscribe to our mailing list? [Y/N]
  // Note: Don't try to generalise the retry logic yet, we will do it next.

  // 3. Implement `retry`, a function that tries to evaluate a block of code.
  // If the code block succeeds, then `retry` returns the result.
  // If the code block fails (exception), then `retry` attempts to evaluate the same expression.
  // If `maxAttempt` is lower than or equal to 0, `retry` throws an exception.
  // For example,
  // retry(5)(2 + 2) == 4 // no retry, 2 + 2 always succeeds the first time.
  //
  // var counter = 0
  // retry(5){ counter += 1; require(counter >= 3)  } // fail twice and then succeeds when counter = 3.
  def retry[A](maxAttempt: Int)(block: => A): A =
    ???

  // 4. Refactor `readUser` using `retry`.

  //////////////////////////////////////////////
  // Bonus question (not covered by the video)
  //////////////////////////////////////////////

  // 5.
  def retryWithError[E, A](maxAttempt: Int)(block: => Either[E, A])(onError: E => Any): A =
    ???
}
