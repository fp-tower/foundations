package answers.action.v2

import java.time.LocalDate

import scala.io.StdIn
import scala.util.Try

object ConsoleApp extends App {
  import ConsoleAnswers._

  imperativeUserConsoleRetry
}

object ConsoleAnswers {

  case class User(name: String, age: Int, createdAt: LocalDate)

  def imperativeUserConsole: Unit = {
    println("What's your name?")
    val name = StdIn.readLine()
    println("What's your age?")
    val age  = StdIn.readLine().toInt
    val now  = LocalDate.now()
    val user = User(name, age, now)
    println(s"User is $user")
  }

  def imperativeUserConsoleRetry: Unit = {
    println("What's your name?")
    val name = StdIn.readLine()
    val age  = readIntV3(maxAttempt = 3)
    val now  = LocalDate.now()
    val user = User(name, age, now)
    println(s"User is $user")
  }

  def readInt(maxAttempt: Int): Int = {
    var age: Option[Int] = None
    var remaining: Int   = maxAttempt

    while (age.isEmpty && remaining > 0) {
      remaining -= 1
      age = Try(StdIn.readLine().toInt).toOption
    }

    age.getOrElse(sys.error("Failed too many times"))
  }

  def readIntV2(maxAttempt: Int): Int = {
    var age: Option[Int] = None
    var remaining: Int   = maxAttempt

    while (age.isEmpty && remaining > 0) {
      remaining -= 1
      age = Try(StdIn.readLine().toInt).toOption
    }

    age.getOrElse(sys.error("Failed too many times"))
  }

  def readIntV3(maxAttempt: Int): Int =
    repeat(maxAttempt, {
      println("What's your age?")
      StdIn.readLine().toInt
    })

  def repeat[A](maxAttempt: Int, thunk: => A): A = {
    var result: Option[A] = None
    var remaining: Int    = maxAttempt

    while (result.isEmpty && remaining > 0) {
      remaining -= 1
      result = Try(thunk).toOption
    }

    result.getOrElse(sys.error("Failed too many times"))
  }

  def repeatRecursive[A](remaining: Int, thunk: => A): A = {
    require(remaining > 0, "Failed too many times")

    if (remaining == 1) thunk
    else
      Try(thunk)
        .getOrElse(repeat(remaining - 1, thunk))
  }

}
