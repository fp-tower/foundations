package sideeffect

import java.time.Instant

import answers.sideeffect.IOAnswers._
import org.scalatest.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.util.{Failure, Try}

class IOAnswersTest extends AnyFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {

  /////////////////////////
  // 1. Smart constructors
  /////////////////////////

  test("succeed") {
    IO.succeed(4).unsafeRun() shouldEqual 4

    forAll((x: Int) => IO.succeed(x).unsafeRun() shouldEqual x)
  }

  test("fail") {
    forAll((e: Exception) => Try(IO.fail(e).unsafeRun()) shouldEqual Failure(e))
  }

  test("effect success") {
    forAll((x: Int) => IO.effect(x).unsafeRun() shouldEqual x)
  }

  test("effect failure") {
    forAll((e: Exception) => Try(IO.effect(throw e).unsafeRun()) shouldEqual Failure(e))
  }

  test("effect is lazy") {
    var called = false
    val io     = IO.effect { called = true }

    called shouldEqual false
    io.unsafeRun()
    called shouldEqual true
  }

  /////////////////////
  // 2. IO API
  /////////////////////

  test("map") {
    forAll((x: Int, f: Int => Boolean) => IO.succeed(x).map(f).unsafeRun() == f(x))
  }

  ////////////////////
  // 4. Testing
  ////////////////////

  ignore("read user from Console") {
    val in: List[String] = List("John", "24")
    val console          = safeTestConsole(in)
    val now              = Instant.ofEpochMilli(100)
    val clock            = testClock(now)

    val user   = userConsoleProgram2(console, clock).unsafeRun()
    val output = console.out.get.unsafeRun()

    user shouldEqual User("John", 24, now)
    output shouldEqual List("What's your name?", "What's your age?")
  }

}
