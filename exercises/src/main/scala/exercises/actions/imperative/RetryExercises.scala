package exercises.actions.imperative

import java.time.LocalDate

import exercises.actions.imperative.UserCreationExercises._

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

object RetryExercises {

  // 1. Implement `readSubscribeToMailingListRetry` which behaves like
  // `readSubscribeToMailingList` but retries when the user enters an invalid input.
  // This methods also prints an error message when it happens.
  // For example,
  // readSubscribeToMailingListRetry(console, maxAttempt = 2)
  // [Prompt] "Would you like to subscribe to our mailing list? [Y/N]"
  // [User] Never
  // [Prompt] Incorrect format, enter "Y" for Yes or "N" for "No"
  // [Prompt] "Would you like to subscribe to our mailing list? [Y/N]"
  // [User] N
  // Returns true. But,
  // readSubscribeToMailingListRetry(console, maxAttempt = 1)
  // [Prompt] "Would you like to subscribe to our mailing list? [Y/N]"
  // [User] Never
  // [Prompt] Incorrect format, enter "Y" for Yes or "N" for "No"
  // Throw an exception because the user had only 1 attempt and they entered an invalid input.
  // Note: `maxAttempt` must be greater than 0, throw an exception if that's not the case.
  // Note: You can implement the retry logic using recursion or a for/while loop. I suggest
  //       to try both version.
  def readSubscribeToMailingListRetry(console: Console, maxAttempt: Int): Boolean =
    ???

  // 2. Implement `readDateOfBirthRetry` which behaves like
  // `readDateOfBirth` but retries when the user enters an invalid input.
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
  // [Prompt] Incorrect format, for example enter "18-03-2001" for 18th of March 2001
  // Throw an exception because the user had only 1 attempt and they entered an invalid input.
  // Note: `maxAttempt` must be greater than 0, throw an exception if that's not the case.
  def readDateOfBirthRetry(console: Console, maxAttempt: Int): LocalDate =
    ???

  // 3. Implement `retry`, a function which evaluates a block of code until either:
  // * It succeeds.
  // * Or the number of attempts is exhausted (when `maxAttempt` is 1).
  // For example,
  // var counter = 0
  // def exec(): String = {
  //   counter += 1
  //   require(counter >= 3, "Counter is too low")
  //   "Hello"
  // }
  // retry(maxAttempt = 5)( () => exec() ) == "Hello"
  // Returns "Hello" because `exec` fails twice and then succeeds when counter reaches 3.
  // retry(maxAttempt = 5){ () => throw new Exception("Boom!") }
  // Throws an exception because `block` fails every time it is evaluated
  // Note: `action: () => A` is a val function which takes 0 argument.
  //       You can create a 0-argument function using the syntax:
  //       * `() => { code }` (recommended syntax)
  //       * `def myMethod() = { code }` and then use eta-expansion to convert
  //          the def function `myMethod` into a val function.
  //       You can execute `action` using `action()`
  // Note: `maxAttempt` must be greater than 0, throw an exception if that's not the case.
  def retry[A](maxAttempt: Int)(action: () => A): A =
    ???

  // 2. Refactor `readSubscribeToMailingListRetry` using
  // `retry` and `readSubscribeToMailingList` (from `UserCreationExercises`).

  // 3. Refactor `readDateOfBirthRetry` using
  // `retry` and `readDateOfBirth` (from `UserCreationExercises`).

  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////

  // 4. Implement `onError` which executes `action`.
  // If an error occurs, it calls the `callBack` function with the error and then rethrow it.
  // For example,
  // onError(() => 1, _ => println("Hello"))
  // print nothing and return 1 because action succeeds.
  // But,
  // onError(() => throw new Exception("Boom"), _ => println("Hello"))
  // print "Hello" and then rethrow the "Boom" exception.
  // Note: What should happen if the `callback` function fails?
  def onError[A](action: () => A, callback: Throwable => Any): A =
    ???

  // 5. Refactor `readSubscribeToMailingList` and `readDateOfBirth` using `onError`

  // 6. Go to UserCreationService

}
