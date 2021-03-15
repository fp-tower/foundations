package answers.action.fp

import java.time.{Instant, LocalDate}
import java.time.format.DateTimeFormatter

object UserCreationAnswers {
  val dateOfBirthFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd-MM-uuuu")

  case class User(
    name: String,
    dateOfBirth: LocalDate,
    subscribedToMailingList: Boolean,
    createdAt: Instant
  )

  def readName(console: Console): Action[String] =
    for {
      _    <- console.writeLine("What's your name")
      name <- console.readLine()
    } yield name

  def readSubscribeToMailingList(console: Console): Action[Boolean] = {
    val action = for {
      _    <- console.writeLine("Would you like to subscribe to our mailing list? [Y/N]")
      bool <- readBoolean(console)
    } yield bool

    action
      .onError(_ => console.writeLine(s"""Incorrect format, enter "Y" for Yes or "N" for "No""""))
  }

  def readDateOfBirth(console: Console): Action[LocalDate] = {
    val action = for {
      _    <- console.writeLine("What's your date of birth? [dd-mm-yyyy]")
      date <- readDate(console, dateOfBirthFormatter)
    } yield date

    action
      .onError(_ => console.writeLine("""Incorrect format, for example enter "18-03-2001" for 18th of March 2001"""))
  }

  def readUser(console: Console, clock: Clock): Action[User] =
    for {
      name       <- readName(console)
      dob        <- readDateOfBirth(console).retry(3)
      subscribed <- readSubscribeToMailingList(console).retry(3)
      now        <- clock.now
    } yield
      User(
        name = name,
        dateOfBirth = dob,
        subscribedToMailingList = subscribed,
        createdAt = now
      )

  def readBoolean(console: Console): Action[Boolean] =
    for {
      line <- console.readLine()
      bool <- parseLineToBoolean(line)
    } yield bool

  def parseLineToBoolean(line: String): Action[Boolean] =
    line match {
      case "Y" => Action(true)
      case "N" => Action(false)
      case _   => Action.fail(new IllegalArgumentException("Invalid input, expected Y/N"))
    }

  def readDate(console: Console, formatter: DateTimeFormatter): Action[LocalDate] =
    for {
      line <- console.readLine()
      date <- Action(LocalDate.parse(line, formatter))
    } yield date
}
