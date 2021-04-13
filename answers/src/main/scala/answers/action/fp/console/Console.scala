package answers.action.fp.console

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import answers.action.fp.IO

import scala.collection.mutable.ListBuffer
import scala.io.StdIn

trait Console {
  def readLine: IO[String]
  def writeLine(message: String): IO[Unit]

  def readYesNo: IO[Boolean] =
    for {
      line <- readLine
      bool <- Console.parseLineToBoolean(line)
    } yield bool

  def readDate(formatter: DateTimeFormatter): IO[LocalDate] =
    for {
      line <- readLine
      date <- IO(LocalDate.parse(line, formatter))
    } yield date
}

object Console {
  def parseLineToBoolean(line: String): IO[Boolean] =
    line match {
      case "Y" => IO(true)
      case "N" => IO(false)
      case _   => IO.fail(new IllegalArgumentException("Invalid input, expected Y/N"))
    }

  def formatBoolean(bool: Boolean): String =
    if (bool) "Y" else "N"

  val system: Console = new Console {
    val readLine: IO[String] =
      IO { StdIn.readLine() }

    def writeLine(message: String): IO[Unit] =
      IO { println(message) }
  }

  def mock(inputs: ListBuffer[String], outputs: ListBuffer[String]): Console = new Console {
    val readLine: IO[String] =
      IO {
        if (inputs.isEmpty) throw new Exception("No input in the console")
        else inputs.remove(0)
      }

    def writeLine(message: String): IO[Unit] =
      IO {
        outputs.append(message)
      }
  }
}
