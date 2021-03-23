package answers.action.fp

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import scala.collection.mutable.ListBuffer
import scala.io.StdIn

trait Console {
  def readLine: Action[String]
  def writeLine(message: String): Action[Unit]

  def readYesNo: Action[Boolean] =
    for {
      line <- readLine
      bool <- Console.parseLineToBoolean(line)
    } yield bool

  def readDate(formatter: DateTimeFormatter): Action[LocalDate] =
    for {
      line <- readLine
      date <- Action(LocalDate.parse(line, formatter))
    } yield date
}

object Console {
  def parseLineToBoolean(line: String): Action[Boolean] =
    line match {
      case "Y" => Action(true)
      case "N" => Action(false)
      case _   => Action.fail(new IllegalArgumentException("Invalid input, expected Y/N"))
    }

  def formatBoolean(bool: Boolean): String =
    if (bool) "Y" else "N"

  val system: Console = new Console {
    val readLine: Action[String] =
      Action { StdIn.readLine() }

    def writeLine(message: String): Action[Unit] =
      Action { println(message) }
  }

  def mock(inputs: ListBuffer[String], outputs: ListBuffer[String]): Console = new Console {
    val readLine: Action[String] =
      Action {
        if (inputs.isEmpty) throw new Exception("No input in the console")
        else inputs.remove(0)
      }

    def writeLine(message: String): Action[Unit] =
      Action {
        outputs.append(message)
      }
  }
}
