package exercises.action.imperative

import java.time.LocalDate

import exercises.action.imperative.UserCreationExercises._

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

  //////////////////////////////////////////////
  // Bonus question (not covered by the video)
  //////////////////////////////////////////////

  // 3. Implement `readUserRetry` that behaves like `readUser` except that
  // it allows the user to make up to 2 mistakes (3 attempts) for reading
  // the date of birth and mailing list subscription flag.
  def readUserRetry(console: Console, clock: Clock): User =
    ???

}
