package answers.action.v3

import LazyAction.delay

import scala.collection.mutable.ListBuffer
import scala.io.StdIn

trait Console {
  def readLine: LazyAction[String]
  def writeLine(message: String): LazyAction[Unit]

  def readInt: LazyAction[Int] =
    readLine.flatMap(ConsoleAnswers.parseToNumber)
}

object Console {
  val system: Console = new Console {
    val readLine: LazyAction[String]                 = delay(StdIn.readLine())
    def writeLine(message: String): LazyAction[Unit] = delay(println(message))
  }

  def test(inputs: ListBuffer[String], outputs: ListBuffer[String]): Console = new Console {
    val readLine: LazyAction[String]                 = delay(inputs.remove(0))
    def writeLine(message: String): LazyAction[Unit] = delay(outputs.append(message))
  }
}
