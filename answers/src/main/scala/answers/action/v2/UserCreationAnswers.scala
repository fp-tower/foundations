package answers.action.v2

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDate}

import scala.io.StdIn
import scala.util.{Failure, Success, Try}

object UserCreationApp extends App {
  import UserCreationAnswers._

  UserCreationAnswers.readUser(
    Console.system,
    Clock.system,
    readDateOfBirthV2(_, dateOfBirthFormatter, 3),
    readSubscribeToMailingListV2(_, 3)
  )
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
    StdIn.readLine() match {
      case "Y"   => true
      case "N"   => false
      case other => throw new IllegalArgumentException(s"$other is not a valid response")
    }
  }

  def readSubscribeToMailingList(console: Console): Boolean = {
    console.writeLine("Would you like to subscribe to our mailing list? [Y/N]")
    console.readLine() match {
      case "Y"   => true
      case "N"   => false
      case other => throw new IllegalArgumentException(s"$other is not a valid response")
    }
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

  def readDateOfBirth(console: Console, formatter: DateTimeFormatter, maxAttempt: Int): LocalDate = {
    var date: Option[LocalDate] = None
    var remaining: Int          = maxAttempt

    while (date.isEmpty && remaining > 0) {
      remaining -= 1
      console.writeLine("What's your date of birth? [dd-mm-yyyy]")
      val line = console.readLine()
      date = Try(LocalDate.parse(line, formatter)).toOption
      if (date.isEmpty) {
        // Can't escape \" with String interpolation, see https://github.com/scala/bug/issues/6476
        console.writeLine("""Incorrect format, for example enter "18-03-2001" for 18th of March 2001""")
      }
    }

    date.getOrElse(sys.error(s"Failed to read a date after $maxAttempt attempts"))
  }

  def readSubscribeToMailingList(console: Console, maxAttempt: Int): Boolean = {
    var subscribed: Option[Boolean] = None
    var remaining: Int              = maxAttempt

    while (subscribed.isEmpty && remaining > 0) {
      remaining -= 1
      console.writeLine("Would you like to subscribe to our mailing list? [Y/N]")
      console.readLine() match {
        case "Y" => subscribed = Some(true)
        case "N" => subscribed = Some(false)
        case _   => console.writeLine(s"""Incorrect format, enter "Y" for Yes or "N" for "No""""")
      }
    }

    subscribed.getOrElse(sys.error(s"Failed to read a date after $maxAttempt attempts"))
  }

  def retry[A](maxAttempt: Int)(block: () => A): A = {
    var error: Throwable  = new IllegalArgumentException("Failed too many times")
    var result: Option[A] = None
    var remaining: Int    = maxAttempt

    while (result.isEmpty && remaining > 0) {
      remaining -= 1
      Try(block()) match {
        case Failure(e)     => error = e
        case Success(value) => result = Some(value)
      }
    }

    result.getOrElse(throw error)
  }

  def retryWithError[A](maxAttempt: Int)(block: => A, onError: Throwable => Any): A = {
    var error: Throwable  = new IllegalArgumentException("Failed too many times")
    var result: Option[A] = None
    var remaining: Int    = maxAttempt

    while (result.isEmpty && remaining > 0) {
      remaining -= 1
      Try(block) match {
        case Failure(e)     => onError(e); error = e
        case Success(value) => result = Some(value)
      }
    }

    result.getOrElse(throw error)
  }

  def readDateOfBirthV2(console: Console, formatter: DateTimeFormatter, maxAttempt: Int): LocalDate =
    retryWithError(maxAttempt)(
      block = {
        console.writeLine("What's your date of birth? [dd-mm-yyyy]")
        val line = console.readLine()
        LocalDate.parse(line, formatter)
      },
      onError = _ => console.writeLine("""Incorrect format, for example enter "18-03-2001" for 18th of March 2001""")
    )

  def readSubscribeToMailingListV2(console: Console, maxAttempt: Int): Boolean =
    retryWithError(maxAttempt)(
      block = {
        console.writeLine("Would you like to subscribe to our mailing list? [Y/N]")
        console.readLine() match {
          case "Y" => true
          case "N" => false
          case _   => throw new IllegalArgumentException(s"""Incorrect format, enter "Y" for Yes or "N" for "No""""")
        }
      },
      onError = e => console.writeLine(e.getMessage)
    )

  def readUser(
    console: Console,
    clock: Clock,
    readDOB: Console => LocalDate,
    readSubscribe: Console => Boolean
  ): User = {
    console.writeLine("What's your name?")
    val name                    = console.readLine()
    val dateOfBirth             = readDOB(console)
    val subscribedToMailingList = readSubscribe(console)
    val now                     = clock.now()
    val user                    = User(name, dateOfBirth, subscribedToMailingList, now)
    console.writeLine(s"User is $user")
    user
  }

}
