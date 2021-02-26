package answers.action.v3

import java.time.LocalDate
import answers.action.v3.LazyAction.delay

import scala.io.StdIn

object ConsoleApp extends App {
  import ConsoleAnswers._

  userConsoleRetryFlatMap.execute()
}

object ConsoleAnswers {

  case class User(name: String, age: Int, today: LocalDate)

  val readLine: LazyAction[String] =
    delay {
      StdIn.readLine()
    }

  val readInt: LazyAction[Int] =
    readLine.andThen(parseToNumber)

  val readToday: LazyAction[LocalDate] =
    delay {
      LocalDate.now()
    }

  val userConsole: LazyAction[User] =
    writeLine("What's your name?").andThen { _ =>
      readLine.andThen { name =>
        writeLine("What's your age?").andThen { _ =>
          readInt.andThen { age =>
            readToday.andThen { today =>
              val user = User(name, age, today)
              writeLine(s"User is $user").map(_ => user)
            }
          }
        }
      }
    }

  val userConsoleRetryFlatMap: LazyAction[User] = {
    val promptAge = writeLine("What's your age?") *> readInt

    for {
      _     <- writeLine("What's your name?")
      name  <- readLine
      age   <- promptAge.retry(3)
      today <- readToday
      user = User(name, age, today)
      _ <- writeLine(s"User is $user")
    } yield user
  }

  def writeLine(message: String): LazyAction[Unit] =
    delay {
      println(message)
    }

  def parseToNumber(line: String): LazyAction[Int] =
    delay {
      line.toInt
    }

}
