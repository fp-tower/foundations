package exercises.actions

import java.time.LocalDate

import scala.io.StdIn

// Run the App using the green arrow next to object (if using IntelliJ)
// or run `sbt` in your terminal to open it in shell mode then type:
// exercises/runMain exercises.actions.ConsoleImperativeExercisesApp
object ConsoleImperativeExercisesApp extends App {
  import ConsoleImperativeExercises._

  createUser()
}

object ConsoleImperativeExercises {

  case class User(name: String, age: Int, createdAt: LocalDate)

  def createUser(): User = {
    println("What's your name?")
    val name = StdIn.readLine()
    println("What's your age?")
    val age  = StdIn.readLine().toInt
    val now  = LocalDate.now()
    val user = User(name, age, now)
    println(s"User is $user")
    user
  }

  // 1. The second question in `createUser` requires a number from the user.
  // Currently, the program will throw an exception if the user supplies an invalid input.
  // Could you add some retry logic to `createUser` so that it attempts up to 3 times to read
  // a number from the command line.
  // Note:

}
