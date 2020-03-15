package sideeffect

import java.time.Instant

import exercises.sideeffect.IOExercises._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Success, Try}

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

  ////////////////////////
  // 4. Testing
  ////////////////////////

  test("read user from Console") {
    val now                     = Instant.now()
    val in: ListBuffer[String]  = ListBuffer("John", "24")
    val out: ListBuffer[String] = ListBuffer.empty[String]
    val console                 = testConsole(in, out)
    val clock                   = testClock(now)

    userConsoleProgram2(console, clock).unsafeRun() shouldEqual User("John", 24, now)
  }

  ////////////////////////
  // 5. Advanced API
  ////////////////////////

  test("deleteTwoOrders") {}

  test("deleteAllUserOrders") {}

}
