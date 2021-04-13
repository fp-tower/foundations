package exercises.action

package object imperative {

  def greeting(): Unit =
    println("Hello")

  // 1. Implement `retry`, a function which evaluates a block of code until either:
  // * It succeeds.
  // * Or the maximum number of attempts are exhausted (when `maxAttempt` is 1).
  // For example,
  // var counter = 0
  // def exec(): String = {
  //   counter += 1
  //   require(counter >= 3, "Counter is too low")
  //   "Hello"
  // }
  // retry(maxAttempt = 5)( exec() ) == "Hello"
  // Returns "Hello" because `exec` fails twice and then succeeds when counter reaches 3.
  // retry(maxAttempt = 5){ throw new Exception("Boom!") }
  // Throws an exception because `block` fails every time it is evaluated
  // Note: `action: () => A` is a val function which takes 0 arguments.
  //       You can create a 0-argument function using the syntax:
  //       * `() => { code }` (recommended syntax)
  //       * `def myMethod() = { code }` and then use eta-expansion to convert
  //          the def function `myMethod` into a val function.
  //       You can execute `action` using `action()`
  // Note: `maxAttempt` must be greater than 0, if not you should throw an exception.
  // Note: Tests are in the `exercises.action.imperative.ImperativeActionTest`
  def retry[A](maxAttempt: Int)(action: => A): A =
    ???

  // 2. Refactor `readSubscribeToMailingListRetry` in `RetryExercises` using `retry`.

  // 3. Refactor `readDateOfBirthRetry` in `RetryExercises` using `retry`.

  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////

  // 4. Implement `onError` which executes `action`.
  // If an error occurs, it calls the `callBack` function with the error and then rethrows it.
  // For example,
  // onError(() => 1, _ => println("Hello"))
  // Will print nothing and return 1 because the action succeeds.
  // But,
  // onError(() => throw new Exception("Boom"), _ => println("Hello"))
  // Will print "Hello" and then rethrow the "Boom" exception.
  // Note: What should happen if the `callback` function fails?
  def onError[A](action: => A, callback: Throwable => Any): A =
    ???

  // 5. Refactor `readSubscribeToMailingListRetry` and `readDateOfBirthRetry` using `onError`

  //////////////////////////////////////////////
  // Bonus question (not covered by the video)
  //////////////////////////////////////////////

  // 6. Implement `retry` using an imperative loop instead of a recursion.

}
