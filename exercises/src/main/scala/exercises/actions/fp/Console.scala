package exercises.actions.fp

import scala.collection.mutable.ListBuffer
import scala.io.StdIn

trait Console {
  def readLine(): Action[String]
  def writeLine(message: String): Action[Unit]
}

object Console {
  val system: Console = new Console {
    def readLine(): Action[String] =
      Action { StdIn.readLine() }

    def writeLine(message: String): Action[Unit] =
      Action { println(message) }
  }

  def mock(inputs: ListBuffer[String], outputs: ListBuffer[String]): Console = new Console {
    def readLine(): Action[String] =
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
