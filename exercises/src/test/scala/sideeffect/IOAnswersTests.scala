package sideeffect

import answers.sideeffect.IOAnswers
import exercises.sideeffect.IOExercises.User
import org.scalatest.Matchers
import org.scalatest.funsuite.AnyFunSuite

class IOAnswersTests extends AnyFunSuite with Matchers {

  ignore("read user from Console") {
    val in: List[String] = List("John", "24")
    val console          = IOAnswers.safeTestConsole(in)

    val user   = IOAnswers.userConsoleProgram4(console).unsafeRun()
    val output = console.out.get.unsafeRun()

    user shouldEqual User("John", 24)
    output shouldEqual List("What's your name?", "What's your age?")
  }

}
