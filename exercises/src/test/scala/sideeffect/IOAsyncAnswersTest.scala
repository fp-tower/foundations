package sideeffect

import java.util.concurrent.ExecutorService

import answers.sideeffect.{IOAsync, IOAsyncRef, IORef}
import exercises.sideeffect.ThreadPoolUtil
import exercises.sideeffect.ThreadPoolUtil.CounterExecutionContext
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Try}

class IOAsyncAnswersTest extends AnyFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {

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

  test("traverse-evalOn") {
    withExecutionContext(ThreadPoolUtil.fixedSize(4, "traverse-evalOn")) { ec =>
      val counterEC = new CounterExecutionContext(ec)

      def bump(ref: IOAsyncRef[Int]): IOAsync[Unit] =
        (IOAsync.printThreadName *> IOAsync.sleep(100.milliseconds) *> ref.update(_ + 1)).evalOn(counterEC)

      val io = for {
        ref <- IOAsyncRef(0)
        _   <- IOAsync.printThreadName
        _   <- IOAsync.traverse(List.fill(10)(0))(_ => bump(ref))
        _   <- IOAsync.printThreadName
        res <- ref.get
      } yield res

      io.unsafeRun() shouldEqual 10
      counterEC.executeCalled.get() shouldEqual 10
    }
  }

  test("concurrentTraverse") {
    withExecutionContext(ThreadPoolUtil.fixedSize(4, "concurrentTraverse-evalOn")) { ec =>
      val counterEC = new CounterExecutionContext(ec)

      def bump(ref: IOAsyncRef[Int]): IOAsync[Unit] =
        IOAsync.printThreadName *> IOAsync.sleep(500.milliseconds) *> ref.update(_ + 1)

      val io = for {
        ref <- IOAsyncRef(0)
        _   <- IOAsync.printThreadName
        _   <- IOAsync.concurrentTraverse(List.fill(10)(0))(_ => bump(ref))(counterEC)
        _   <- IOAsync.printThreadName
        res <- ref.get
      } yield res

      io.unsafeRun() shouldEqual 10
      counterEC.executeCalled.get() shouldEqual 10
    }
  }

  test("concurrentMap2") {
    withExecutionContext(ThreadPoolUtil.fixedSize(4, "concurrentMap2")) { ec =>
      val counterEC = new CounterExecutionContext(ec)

      def bump(ref: IOAsyncRef[Int]): IOAsync[Unit] =
        IOAsync.printThreadName *> ref.updateGetNew(_ + 1).map(_.toString).flatMap(IOAsync.printLine)

      val io = for {
        ref <- IOAsyncRef(0)
        _   <- bump(ref).concurrentTuple(bump(ref))(counterEC)
        res <- ref.get
      } yield res

      io.unsafeRun() shouldEqual 2
      counterEC.executeCalled.get() shouldEqual 2
    }
  }

  test("evalOn - async") {
    withExecutionContext(ThreadPoolUtil.fixedSize(2, "ec1")) { ec1 =>
      withExecutionContext(ThreadPoolUtil.fixedSize(2, "ec2")) { ec2 =>
        withExecutionContext(ThreadPoolUtil.fixedSize(2, "ec3")) { ec3 =>
          withExecutionContext(ThreadPoolUtil.fixedSize(2, "ec4")) { ec4 =>
            val asyncPrint = IOAsync.async[Unit](cb => cb(Right(println("cb: " + Thread.currentThread.getName))))(ec4)
            val io         = asyncPrint *> IOAsync.effect(println("io: " + Thread.currentThread.getName))

            (io.evalOn(ec1) *> io.evalOn(ec2)).evalOn(ec3).unsafeRun()
          }
        }
      }
    }
  }

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

  test("retryOnce") {
    val error = new Exception("Unsupported odd number")
    def action(ref: IOAsyncRef[Int]): IOAsync[String] =
      ref.updateGetNew(_ + 1).map(_ % 2 == 0).flatMap {
        case true  => IOAsync.succeed("OK")
        case false => IOAsync.fail(error)
      }

    IOAsyncRef(0).flatMap(action).attempt.unsafeRun() shouldEqual Left(error)
    IOAsyncRef(0).flatMap(action(_).retryOnce).unsafeRun() shouldEqual "OK"
  }

  // TODO use Resource or handmade equivalent
  def withExecutionContext[A](makeES: => ExecutorService)(f: ExecutionContext => A): A = {
    val es = makeES
    val ec = ExecutionContext.fromExecutorService(es)
    val a  = f(ec)
    es.shutdown()
    a
  }

}
