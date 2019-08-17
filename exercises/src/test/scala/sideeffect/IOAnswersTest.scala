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
    forAll((e: Exception, f: Int => Boolean) => IO.fail(e).map(f).attempt.unsafeRun() == Left(e))
  }

  test("attempt") {
    forAll((x: Int) => IO.succeed(x).attempt.unsafeRun() shouldEqual Right(x))
    forAll((e: Exception) => IO.fail(e).attempt.unsafeRun() shouldEqual Left(e))
  }

  ////////////////////
  // 4. Testing
  ////////////////////

  test("read user from Console") {
    val in: List[String] = List("John", "24")
    val console          = safeTestConsole(in)
    val now              = Instant.ofEpochMilli(100)
    val clock            = testClock(now)

    val user   = userConsoleProgram2(console, clock).unsafeRun()
    val output = console.out.get.unsafeRun()

    user shouldEqual User("John", 24, now)
    output shouldEqual List("What's your name?", "What's your age?")
  }

  ////////////////////////
  // 5. Advanced API
  ////////////////////////

  test("traverse") {
    forAll((xs: List[Int]) => traverse(xs)(IO.succeed).unsafeRun() shouldEqual xs)

    forAll { xs: List[Exception] =>
      val boom = new Exception("boom")
      traverse(boom :: xs)(IO.fail).attempt.unsafeRun() shouldEqual Left(boom)
    }
  }

}
