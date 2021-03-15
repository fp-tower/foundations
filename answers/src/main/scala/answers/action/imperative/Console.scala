package answers.action.imperative

import scala.collection.mutable.ListBuffer
import scala.io.StdIn

trait Console {
  def readLine(): String
  def writeLine(message: String): Unit
}

object Console {
  val system: Console = new Console {
    def readLine(): String               = StdIn.readLine()
    def writeLine(message: String): Unit = println(message)
  }

  def mock(inputs: ListBuffer[String], outputs: ListBuffer[String]): Console = new Console {
    def readLine(): String =
      if (inputs.isEmpty) throw new Exception("No input in the console")
      else inputs.remove(0)

    def writeLine(message: String): Unit =
      outputs.append(message)
  }
}
