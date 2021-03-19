package answers.action.imperative

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import scala.io.StdIn
import scala.util.Try

object UserCreationApp extends App {
  import UserCreationAnswers._

  readUser(Console.system, Clock.system)
}

object UserCreationAnswers {
  // If you use `y` you should also use `G`. Since `G` is rarely used,
  // the correct year symbol is `u`, not `y`, otherwise a non-positive year
  // will show incorrectly.
  // https://stackoverflow.com/a/41178418
  val dateOfBirthFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd-MM-uuuu")

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
    parseDate(console.readLine())
  }

  def parseDate(line: String): LocalDate =
    Try(LocalDate.parse(line, dateOfBirthFormatter))
      .getOrElse(
        throw new IllegalArgumentException(s"Expected a date with format dd-mm-yyyy but received $line")
      )

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

}
