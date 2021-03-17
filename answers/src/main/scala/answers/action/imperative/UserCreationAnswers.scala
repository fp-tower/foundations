package answers.action.imperative

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDate}

import answers.action.imperative.RetryAnswers.{onError, retry, retryWithError}

import scala.annotation.tailrec
import scala.io.StdIn
import scala.util.{Failure, Success, Try}

object UserCreationApp extends App {
  import UserCreationAnswers._

  readUserRetry(Console.system, Clock.system)
}

object UserCreationAnswers {
  // If you use `y` you should also use `G`. Since `G` is rarely used,
  // the correct year symbol is `u`, not `y`, otherwise a non-positive year
  // will show incorrectly.
  // https://stackoverflow.com/a/41178418
  val dateOfBirthFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd-MM-uuuu")

  case class User(
    name: String,
    dateOfBirth: LocalDate,
    subscribedToMailingList: Boolean,
    createdAt: Instant
  )

  def readSubscribeToMailingList(): Boolean = {
    println("Would you like to subscribe to our mailing list? [Y/N]")
    parseYesNo(StdIn.readLine())
  }

  def parseYesNo(line: String): Boolean =
    line match {
      case "Y"   => true
      case "N"   => false
      case other => throw new IllegalArgumentException(s"""Expected "Y" or "N" but received $other""")
    }

  def readSubscribeToMailingList(console: Console): Boolean = {
    console.writeLine("Would you like to subscribe to our mailing list? [Y/N]")
    parseYesNo(console.readLine())
  }

  def readDateOfBirth(console: Console): LocalDate = {
    console.writeLine("What's your date of birth? [dd-mm-yyyy]")
    LocalDate.parse(console.readLine(), dateOfBirthFormatter)
  }

  def readUser(console: Console, clock: Clock): User = {
    console.writeLine("What's your name?")
    val name                    = console.readLine()
    val dateOfBirth             = readDateOfBirth(console)
    val subscribedToMailingList = readSubscribeToMailingList(console)
    val now                     = clock.now()
    val user                    = User(name, dateOfBirth, subscribedToMailingList, now)
    console.writeLine(s"User is $user")
    user
  }

  @tailrec
  def readSubscribeToMailingListRetry(console: Console, maxAttempt: Int): Boolean = {
    require(maxAttempt > 0, "maxAttempt must be greater than 0")

    console.writeLine("Would you like to subscribe to our mailing list? [Y/N]")
    val line = console.readLine()
    Try(parseYesNo(line)) match {
      case Success(value) => value
      case Failure(error) =>
        console.writeLine("""Incorrect format, enter "Y" for Yes or "N" for "No"""")
        if (maxAttempt == 1) throw error
        else readSubscribeToMailingListRetry(console, maxAttempt - 1)
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
    Try(LocalDate.parse(line, dateOfBirthFormatter)) match {
      case Success(value) => value
      case Failure(error) =>
        console.writeLine("""Incorrect format, for example enter "18-03-2001" for 18th of March 2001""")
        if (maxAttempt == 1) throw error
        else readDateOfBirthRetry(console, maxAttempt - 1)
    }
  }

  def readSubscribeToMailingListRetryV2(console: Console, maxAttempt: Int): Boolean =
    retry(maxAttempt)(
      block = () =>
        onError(
          block = () => {
            console.writeLine("Would you like to subscribe to our mailing list? [Y/N]")
            console.readLine() match {
              case "Y" => true
              case "N" => false
              case _   => throw new IllegalArgumentException(s"""Incorrect format, enter "Y" for Yes or "N" for "No"""")
            }
          },
          callback = e => console.writeLine(e.getMessage)
      )
    )

  def readDateOfBirthRetryV2(console: Console, maxAttempt: Int): LocalDate =
    retryWithError(maxAttempt)(
      block = () => {
        console.writeLine("What's your date of birth? [dd-mm-yyyy]")
        val line = console.readLine()
        LocalDate.parse(line, dateOfBirthFormatter)
      },
      onError = _ => console.writeLine("""Incorrect format, for example enter "18-03-2001" for 18th of March 2001""")
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
