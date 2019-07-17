package exercises.sideeffect

import scala.io.StdIn

object Console {
  def readLine: IO[String] =
    IO.effect(StdIn.readLine())

  def writeLine(message: String): IO[Unit] =
    IO.effect(println(message))
}

