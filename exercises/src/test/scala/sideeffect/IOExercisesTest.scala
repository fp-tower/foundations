package sideeffect

import exercises.sideeffect.IOExercises.{testConsole, IO}
import org.scalatest.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Try}

class IOExercisesTest extends AnyFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {

  /////////////////////////
  // 1. Smart constructors
  /////////////////////////

  test("succeed") {
    IO.succeed(4).unsafeRun() shouldEqual 4

    forAll((x: Int) => IO.succeed(x).unsafeRun() shouldEqual x)
  }

  test("fail") {
    // TODO
  }

  test("effect") {
    // TODO
  }

  /////////////////////
  // 2. IO API
  /////////////////////

  test("map") {
    // TODO
  }

  ////////////////////
  // 3. Programs
  ////////////////////

  test("read user from Console") {
    val in: List[String]        = ???
    val out: ListBuffer[String] = new ListBuffer[String]()
    val console                 = testConsole(in, out)

  }

}
