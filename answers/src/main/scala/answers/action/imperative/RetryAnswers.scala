package answers.action.imperative

import java.time.LocalDate

import answers.action.imperative.UserCreationAnswers._

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

object RetryAnswers {

  @tailrec
  def readSubscribeToMailingListRetry(console: Console, maxAttempt: Int): Boolean = {
    require(maxAttempt > 0, "maxAttempt must be greater than 0")

    console.writeLine("Would you like to subscribe to our mailing list? [Y/N]")
    val line = console.readLine()
    Try(parseYesNo(line)) match {
      case Success(value) => value
      case Failure(error) =>
        console.writeLine("""Incorrect format, enter "Y" for Yes or "N" for "No"""")
        if (maxAttempt > 1) readSubscribeToMailingListRetry(console, maxAttempt - 1)
        else throw error
    }
  }

  // same but with a while loop instead of recursion
  def readSubscribeToMailingListRetryWhileLoop(console: Console, maxAttempt: Int): Boolean = {
    var subscribed: Try[Boolean] = Failure(new IllegalArgumentException("maxAttempt must be greater than 0"))
    var remaining: Int           = maxAttempt

    while (subscribed.isFailure && remaining > 0) {
      remaining -= 1
      console.writeLine("Would you like to subscribe to our mailing list? [Y/N]")
      subscribed = Try(parseYesNo(console.readLine()))

      if (subscribed.isFailure)
        console.writeLine(s"""Incorrect format, enter "Y" for Yes or "N" for "No"""")
    }

    subscribed.get
  }

  @tailrec
  def readDateOfBirthRetry(console: Console, maxAttempt: Int): LocalDate = {
    require(maxAttempt > 0, "maxAttempt must be greater than 0")

    console.writeLine("What's your date of birth? [dd-mm-yyyy]")
    val line = console.readLine()
    Try(parseDate(line)) match {
      case Success(value) => value
      case Failure(error) =>
        console.writeLine("""Incorrect format, for example enter "18-03-2001" for 18th of March 2001""")
        if (maxAttempt > 1) readDateOfBirthRetry(console, maxAttempt - 1)
        else throw error
    }
  }

  def readSubscribeToMailingListRetryV2(console: Console, maxAttempt: Int): Boolean =
    retry(maxAttempt)(
      action = onError(
        action = readSubscribeToMailingList(console),
        callback = _ => console.writeLine(s"""Incorrect format, enter "Y" for Yes or "N" for "No"""")
      )
    )

  def readDateOfBirthRetryV2(console: Console, maxAttempt: Int): LocalDate =
    retry(maxAttempt)(
      action = onError(
        action = readDateOfBirth(console),
        callback = _ => console.writeLine("""Incorrect format, for example enter "18-03-2001" for 18th of March 2001""")
      )
    )

  def readUserRetry(console: Console, clock: Clock): User = {
    console.writeLine("What's your name?")
    val name                    = console.readLine()
    val dateOfBirth             = readDateOfBirthRetryV2(console, 3)
    val subscribedToMailingList = readSubscribeToMailingListRetryV2(console, 3)
    val now                     = clock.now()
    val user                    = User(name, dateOfBirth, subscribedToMailingList, now)
    console.writeLine(s"User is $user")
    user
  }

}
