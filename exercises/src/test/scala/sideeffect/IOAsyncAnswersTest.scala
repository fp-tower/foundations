package sideeffect

import java.time.Instant

import exercises.sideeffect.IOAsync
import exercises.sideeffect.IORef
import org.scalatest.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.concurrent.Future
import scala.util.{Failure, Try}

class IOAsyncAnswersTest extends AnyFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {

  /////////////////////////
  // 1. Smart constructors
  /////////////////////////

  test("succeed") {
    IOAsync.succeed(4).unsafeRun() shouldEqual 4

    forAll((x: Int) => IOAsync.succeed(x).unsafeRun() shouldEqual x)
  }

  test("fail") {
    forAll((e: Exception) => Try(IOAsync.fail(e).unsafeRun()) shouldEqual Failure(e))
  }

  test("effect success") {
    forAll((x: Int) => IOAsync.effect(x).unsafeRun() shouldEqual x)
  }

  test("effect failure") {
    forAll((e: Exception) => Try(IOAsync.effect(throw e).unsafeRun()) shouldEqual Failure(e))
  }

  test("effect is lazy") {
    var called = false
    val io     = IOAsync.effect { called = true }

    called shouldEqual false
    io.unsafeRun()
    called shouldEqual true
  }

  test("fromFuture") {
    var counter     = 0
    lazy val future = Future { counter += 1 }(scala.concurrent.ExecutionContext.global)

    val io = IOAsync.fromFuture(future)
    counter shouldEqual 0

    io.unsafeRun()
    counter shouldEqual 1
  }

  /////////////////////
  // 2. IO API
  /////////////////////

  test("map") {
    forAll((x: Int, f: Int => Boolean) => IOAsync.succeed(x).map(f).unsafeRun() == f(x))
    forAll((e: Exception, f: Int => Boolean) => IOAsync.fail(e).map(f).attempt.unsafeRun() == Left(e))
  }

  test("flatMap") {
    forAll { (x: Int, f: Int => Int) =>
      IORef(x).flatMap(_.updateGetNew(f)).unsafeRun() shouldEqual f(x)
    }

    forAll(
      (e: Exception) => IOAsync.fail(e).flatMap(_ => IOAsync.notImplemented).attempt.unsafeRun() shouldEqual Left(e)
    )

    forAll(
      (x: Int, e: Exception) => IOAsync.fail(e).flatMap(_ => IOAsync.succeed(x)).attempt.unsafeRun() shouldEqual Left(e)
    )
  }

  test("attempt") {
    forAll((x: Int) => IOAsync.succeed(x).attempt.unsafeRun() shouldEqual Right(x))
    forAll((e: Exception) => IOAsync.fail(e).attempt.unsafeRun() shouldEqual Left(e))
  }

}
