package exercises.action

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

package object imperative {

  def greeting(): Unit =
    println("Hello")

  // 1. Implement `retry`, a function which evaluates a block of code until either:
  // * It succeeds.
  // * Or the maximum number of attempts are exhausted (when `maxAttempt` is 1).
  // For example,
  // var counter = 0
  // retry(maxAttempt = 5){
  //   counter += 1
  //   require(counter >= 3, "Counter is too low")
  //   "Hello"
  // }
  // Returns "Hello" because `action` fails twice and then succeeds when counter reaches 3.
  // However,
  // retry(maxAttempt = 5){ throw new Exception("Boom!") }
  // Throws an exception because `action` fails every time it is evaluated
  // Note: `action: => A` is a by-name parameter (see the Evaluation lesson).
  // Note: `maxAttempt` must be greater than 0, if not you should throw an exception.
  // Note: Tests are in the `exercises.action.imperative.ImperativeActionTest`
  def retry[A](maxAttempt: Int)(action: => A): A =
    ???

  // 2. Refactor `readSubscribeToMailingListRetry` in `UserCreationExercises` using `retry`.

  // 3. Implement `onError` which executes `action`.
  // If an error occurs, it calls the `cleanup` function with the error and then rethrows it.
  // For example,
  // onError(1, _ => println("Hello"))
  // Prints nothing and return 1 because `action` is a success.
  // But,
  // onError(throw new Exception("Boom"), e => println("An error occurred: ${e.getMessage}"))
  // Prints "An error occurred: Boom" and then rethrow the "Boom" exception.
  // Note: You need to write tests for `onError` yourself in `exercises.action.imperative.ImperativeActionTest`
  def onError[A](action: => A, cleanup: Throwable => Any): A =
    ???

  // 4. Refactor `readSubscribeToMailingListRetry` using `onError` in `UserCreationExercises`.

  // 5. Refactor `readDateOfBirthRetry` using `retry` and `onError` in `UserCreationExercises`.

  //////////////////////////////////////////////
  // Bonus questions (not covered by the video)
  //////////////////////////////////////////////

  // 6. Write a property based for `retry`. For example,
  // Step 1. Generate a function that throws an exception for the first `n` evaluations.
  // Step 2. Generate a random value for `maxAttempt`.
  // Step 3. Check `retry` is a success if `maxAttempt > number of errors` and a failure otherwise.

  // 7. Implement `retry` using an imperative loop instead of a recursion.

  // 8. `onError` takes a `cleanup` function which can fail.
  // This means we could end up with two exceptions:
  // * One from `action`
  // * One from `cleanup`
  // For example, if both `emailClient.send` and `db.saveUnsentEmail` fail
  // onError(
  //   action  = emailClient.send(email),
  //   cleanup = db.saveUnsentEmail(email, exception.getMessage)
  // )
  // In this case, we would like `onError` to swallow the error from `cleanup` and
  // rethrow the error from `action`.
  // Add a test case for this scenario and update `onError` implementation.

}
