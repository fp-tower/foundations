package exercises.action.fp.console

import exercises.action.fp.IO

import scala.collection.mutable.ListBuffer
import scala.io.StdIn

trait Console {
  def readLine: IO[String]
  def writeLine(message: String): IO[Unit]
}

object Console {
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
