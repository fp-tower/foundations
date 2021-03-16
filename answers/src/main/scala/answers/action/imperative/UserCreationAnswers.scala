package answers.action.imperative

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDate}

import answers.action.imperative.RetryAnswers.{onError, retry, retryWithError}

import scala.io.StdIn
import scala.util.{Failure, Success, Try}

object UserCreationApp extends App {
  import UserCreationAnswers._

  readUserRetry(Console.system, Clock.system)
}

object UserCreationAnswers {
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

  def readSubscribeToMailingListRetry(console: Console, maxAttempt: Int): Boolean = {
    var subscribed: Option[Boolean] = None
    var remaining: Int              = maxAttempt

    while (subscribed.isEmpty && remaining > 0) {
      remaining -= 1
      console.writeLine("Would you like to subscribe to our mailing list? [Y/N]")
      Try(parseYesNo(console.readLine())) match {
        case Success(bool) => subscribed = Some(bool)
        case Failure(_)    => console.writeLine(s"""Incorrect format, enter "Y" for Yes or "N" for "No"""")
      }
    }

    subscribed.getOrElse(sys.error(s"Failed to read a boolean after $maxAttempt attempts"))
  }

  def readDateOfBirthRetry(console: Console, maxAttempt: Int): LocalDate = {
    var date: Option[LocalDate] = None
    var remaining: Int          = maxAttempt

    while (date.isEmpty && remaining > 0) {
      remaining -= 1
      console.writeLine("What's your date of birth? [dd-mm-yyyy]")
      val line = console.readLine()
      Try(LocalDate.parse(line, dateOfBirthFormatter)) match {
        case Success(value) => date = Some(value)
        case Failure(_) =>
          console.writeLine("""Incorrect format, for example enter "18-03-2001" for 18th of March 2001""")
      }
    }

    date.getOrElse(sys.error(s"Failed to read a date after $maxAttempt attempts"))
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
