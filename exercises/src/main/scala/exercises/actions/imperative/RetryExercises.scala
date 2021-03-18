package exercises.actions.imperative

object RetryExercises {

  // 1. Implement `retry`, a function which evaluates a block of code until it succeeds
  // or the number of attempts is exhausted. For example,
  // var counter = 0
  // def exec(): String = {
  //   counter += 1
  //   require(counter >= 3, "Counter is too low")
  //   "Hello"
  // }
  // retry(maxAttempt = 5)(exec) == "Hello"
  // Returns "Hello" because `exec` fails twice and then succeeds when counter reaches 3.
  // retry(maxAttempt = 5){ () => throw new Exception("Boom!") }
  // Throws an exception because `block` fails every time it is evaluated
  // Note: `action: () => A` is a val function which takes 0 argument.
  //       You can execute `block` using `action()`
  // Note: `maxAttempt` must be greater than 0, throw an exception if that's not the case.
  def retry[A](maxAttempt: Int)(action: () => A): A =
    ???

  // 2. Refactor `readSubscribeToMailingListRetry` using `retry`.

  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////

  // 3. Implement `retryWithError` which provides an `onError` callback.
  // `onError` is called whenever `block` fails. For example,
  // var counter = 0
  // def exec(): String = {
  //   counter += 1
  //   if(counter < 3) throw new Exception("Boom!")
  //   else "Hello"
  // }
  // val result = retryWithError(maxAttempt = 5)(
  //   action  = exec,
  //   onError = _ => println("An error occurred, counter is $counter")
  // )
  // print "An error occurred, counter is 0"
  // print "An error occurred, counter is 1"
  // print "An error occurred, counter is 2"
  // result == "Hello"
  def retryWithError[A](maxAttempt: Int)(action: () => A, onError: Throwable => Any): A =
    ???

  //////////////////////////////////////////////
  // Bonus question (not covered by the video)
  //////////////////////////////////////////////

  // 4. `retryWithError` does two things:
  // * `retry` an action a few times
  // * call a callback when an error occurs
  // Ideally, we would separate these two responsibilities in two
  // separate functions (single-responsibility principle).
  //
  // Implement `onError` which executes `block`.
  // If an error occurs, it also calls `callBack` and then rethrow the original exception.
  // For example,
  // onError(() => 1, _ => println("Hello"))
  // print nothing and return 1.
  // But,
  // onError(() => throw new Exception("Boom"), _ => println("Hello"))
  // print "Hello" and then throw an Exception.
  def onError[A](action: () => A, callback: Throwable => Any): A =
    ???

  // 5. Write a property-based test which verifies `retryWithError` is consistent
  // with `retry` and `onError` used together.

}
