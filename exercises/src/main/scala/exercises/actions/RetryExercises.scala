package exercises.actions

object RetryExercises {

  // 1. Implement `retry`, a function that evaluate a block of code until it succeeds or
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
  // Note: `block: () => A` is a val function which takes 0 argument.
  //       You can execute `block` using `block()`
  def retry[A](maxAttempt: Int)(block: () => A): A =
    ???

  // 2. Refactor `readSubscribeToMailingListRetry` and `readDateOfBirthRetry` using `retry`.

  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////

  // 3. Refactor `readDateOfBirth` and `readSubscribeToMailingList` to use `retry`.
  // Does it produce the same result?
  // If not, try to implement `retryWithError` which provides an `onError` callback.
  // `onError` should be called whenever `block` fails. For example,
  // `onError = error => println("Oops: " + error.getMessage)`
  def retryWithError[A](maxAttempt: Int)(block: () => A, onError: Throwable => Any): A =
    ???

}
