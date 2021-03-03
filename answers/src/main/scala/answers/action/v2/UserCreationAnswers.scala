package answers.action.v2

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDate}

import scala.io.StdIn
import scala.util.{Failure, Success, Try}

object UserCreationApp extends App {
  import UserCreationAnswers._

  UserCreationAnswers.readUser(
    readDateOfBirthV2(dobFormatter, 3),
    readSubscribeToMailingListV2(3)
  )
}

object UserCreationAnswers {
  val dobFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

  case class User(
    name: String,
    dateOfBirth: LocalDate,
    subscribedToMailingList: Boolean,
    createdAt: Instant
  )

  def readUser(
    readDOB: => LocalDate,
    readSubscribe: => Boolean
  ): User = {
    println("What's your name?")
    val name                    = StdIn.readLine()
    val dateOfBirth             = readDOB
    val subscribedToMailingList = readSubscribe
    val now                     = Instant.now()
    val user                    = User(name, dateOfBirth, subscribedToMailingList, now)
    println(s"User is $user")
    user
  }

  def readDateOfBirth(formatter: DateTimeFormatter, maxAttempt: Int): LocalDate = {
    var date: Option[LocalDate] = None
    var remaining: Int          = maxAttempt

    while (date.isEmpty && remaining > 0) {
      remaining -= 1
      println("What's your date of birth (dd-mm-yyyy)?")
      val line = StdIn.readLine()
      date = Try(LocalDate.parse(line, formatter)).toOption
      if (date.isEmpty) {
        // Can't escape \" with String interpolation, see https://github.com/scala/bug/issues/6476
        println(s""""$line" is is not a valid date""")
      }
    }

    date.getOrElse(sys.error(s"Failed to read a date after $maxAttempt attempts"))
  }

  def readSubscribeToMailingList(maxAttempt: Int): Boolean = {
    var subscribed: Option[Boolean] = None
    var remaining: Int              = maxAttempt

    while (subscribed.isEmpty && remaining > 0) {
      remaining -= 1
      println("Would you like to subscribe to our mailing list? [Y/N]")
      StdIn.readLine() match {
        case "Y"   => subscribed = Some(true)
        case "N"   => subscribed = Some(false)
        case other => println(s"""Incorrect answer, you must answer with "Y" for Yes or "N" for "No"""")
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

  def readDateOfBirthV2(formatter: DateTimeFormatter, maxAttempt: Int): LocalDate =
    retry(maxAttempt) { () =>
      println("What's your date of birth (dd-mm-yyyy)?")
      val line = StdIn.readLine()
      LocalDate.parse(line, formatter)
    }

  def readSubscribeToMailingListV2(maxAttempt: Int): Boolean =
    retry(maxAttempt) { () =>
      println("Would you like to subscribe to our mailing list? [Y/N]")
      StdIn.readLine() match {
        case "Y"   => true
        case "N"   => false
        case other => throw new IllegalArgumentException(s"""Expected "Y" or "N", but received "$other"""")
      }
    }

  def retryWithError[A](maxAttempt: Int)(block: => A)(onError: Throwable => Any): A = {
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

  def retryWithErrorV2[E, A](maxAttempt: Int)(block: => Either[E, A])(onError: E => Any): A = {
    require(maxAttempt > 0, "maxAttempt must be bigger than 0")
    var result: Option[A] = None
    var remaining: Int    = maxAttempt

    while (result.isEmpty && remaining > 0) {
      remaining -= 1
      block match {
        case Left(e)      => onError(e)
        case Right(value) => result = Some(value)
      }
    }

    result.getOrElse(sys.error("Failed too many times"))
  }

  def readDateOfBirthV3(formatter: DateTimeFormatter, maxAttempt: Int): LocalDate =
    retryWithErrorV2(maxAttempt) {
      println("What's your date of birth (dd-mm-yyyy)?")
      val line = StdIn.readLine()
      Try(LocalDate.parse(line, formatter)).toEither.left.map(_ => line)
    }(line => println(s""""$line" is is not a valid date"""))

  def readSubscribeToMailingListV3(maxAttempt: Int): Boolean =
    retryWithErrorV2(maxAttempt) {
      println("Would you like to subscribe to our mailing list? [Y/N]")
      StdIn.readLine() match {
        case "Y"   => Right(true)
        case "N"   => Right(false)
        case other => Left(other)
      }
    }(line => println(s""""$line" is is not a valid, expected "Y" or "N""""))

}
