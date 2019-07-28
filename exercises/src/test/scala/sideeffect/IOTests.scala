package sideeffect

import exercises.sideeffect.IOExercises
import org.scalatest.Matchers
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable.ListBuffer

class IOTests extends AnyFunSuite with Matchers {

  test("read user from Console") {
    val in: List[String]        = ???
    val out: ListBuffer[String] = new ListBuffer[String]()
    val console                 = IOExercises.testConsole(in, out)

  }

}
