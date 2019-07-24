package exercises.sideeffect

import scala.io.StdIn

object Console {
  def readLine: IO[String] =
    IO(StdIn.readLine())

  def writeLine(message: String): IO[Unit] =
    IO(println(message))
}
