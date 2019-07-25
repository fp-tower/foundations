package exercises.sideeffect

import scala.io.StdIn

object AlgebraExercises {

  sealed trait Console[A]
  object Console {
    case object ReadLine                  extends Console[String]
    case class WriteLine(message: String) extends Console[Unit]
  }

  def unsafeRunConsole[A](console: Console[A]): A =
    console match {
      case Console.ReadLine     => StdIn.readLine()
      case Console.WriteLine(x) => println(x)
    }

}
