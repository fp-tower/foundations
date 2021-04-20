package answers.action.imperative

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import scala.util.Try

object UserCreationServiceApp extends App {
  val console = Console.system
  val clock   = Clock.system
  val service = new UserCreationService(console, clock)

  service.readUser()
}

class UserCreationService(console: Console, clock: Clock) {
  import UserCreationService._

  def readName(): String = {
    console.writeLine("What's your name?")
    console.readLine()
  }

  def readDateOfBirth(): LocalDate = {
    console.writeLine("What's your date of birth? [dd-mm-yyyy]")
    onError(
      action = parseDateOfBirth(console.readLine()),
      cleanup = _ => console.writeLine("""Incorrect format, for example enter "18-03-2001" for 18th of March 2001""")
    )
  }

  def readSubscribeToMailingList(): Boolean = {
    console.writeLine("Would you like to subscribe to our mailing list? [Y/N]")
    onError(
      action = parseYesNo(console.readLine()),
      cleanup = _ => console.writeLine("""Incorrect format, enter "Y" for Yes or "N" for "No"""")
    )
  }

  def readUser(): User = {
    val name        = readName()
    val dateOfBirth = retry(3)(readDateOfBirth())
    val subscribed  = retry(3)(readSubscribeToMailingList())
    val now         = clock.now()
    val user        = User(name, dateOfBirth, subscribed, now)
    console.writeLine(s"User is $user")
    user
  }

}

object UserCreationService {

  val dateOfBirthFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd-MM-uuuu")

  def parseYesNo(line: String): Boolean =
    line match {
      case "Y"   => true
      case "N"   => false
      case other => throw new IllegalArgumentException(s"""Expected "Y" or "N" but received $other""")
    }

  def formatYesNo(bool: Boolean): String =
    if (bool) "Y" else "N"

  def parseDateOfBirth(line: String): LocalDate =
    Try(LocalDate.parse(line, dateOfBirthFormatter))
      .getOrElse(
        throw new IllegalArgumentException(s"Expected a date with format dd-mm-yyyy but received $line")
      )

  def formatDateOfBirth(date: LocalDate): String =
    dateOfBirthFormatter.format(date)

}
