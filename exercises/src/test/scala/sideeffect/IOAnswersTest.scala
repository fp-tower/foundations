package sideeffect

import java.time.Instant

import answers.sideeffect.IOAnswers._
import exercises.sideeffect.IORef
import org.scalatest.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.util.{Failure, Success, Try}

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
    forAll((x: Int, f: Int => Boolean) => IO.succeed(x).map(f).unsafeRun() shouldEqual f(x))
    forAll((e: Exception, f: Int => Boolean) => IO.fail(e).map(f).attempt.unsafeRun() shouldEqual Failure(e))
  }

  test("flatMap") {
    forAll { (x: Int, f: Int => Int) =>
      IORef(x).flatMap(_.updateGetNew(f)).unsafeRun() shouldEqual f(x)
    }

    forAll((e: Exception) => IO.fail(e).flatMap(_ => IO.notImplemented).attempt.unsafeRun() shouldEqual Failure(e))

    forAll((x: Int, e: Exception) => IO.fail(e).flatMap(_ => IO.succeed(x)).attempt.unsafeRun() shouldEqual Failure(e))
  }

  test("attempt") {
    forAll((x: Int) => IO.succeed(x).attempt.unsafeRun() shouldEqual Success(x))
    forAll((e: Exception) => IO.fail(e).attempt.unsafeRun() shouldEqual Failure(e))
  }

  test("retryOnce") {
    val error = new Exception("Unsupported odd number")
    def action(ref: IORef[Int]): IO[String] =
      ref.updateGetNew(_ + 1).map(_ % 2 == 0).flatMap {
        case true  => IO.succeed("OK")
        case false => IO.fail(error)
      }

    IORef(0).flatMap(action).attempt.unsafeRun() shouldEqual Failure(error)
    IORef(0).flatMap(action(_).retryOnce).unsafeRun() shouldEqual "OK"
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
      traverse(boom :: xs)(IO.fail).attempt.unsafeRun() shouldEqual Failure(boom)
    }
  }

  test("deleteAllUserOrders") {
    val users = List(User_V2(UserId("1234"), "Rob", List(OrderId("1111"), OrderId("5555"))))
    def testApi(ref: IORef[List[OrderId]]) = new UserOrderApi {
      def getUser(userId: UserId): IO[User_V2] =
        users.find(_.userId == userId) match {
          case None    => IO.fail(new Exception(s"User not found $userId"))
          case Some(u) => IO.succeed(u)
        }

      def deleteOrder(orderId: OrderId): IO[Unit] =
        ref.update(_ :+ orderId)
    }

    (for {
      ref <- IORef(List.empty[OrderId])
      api = testApi(ref)
      _   <- deleteAllUserOrders(api)(UserId("1234"))
      ids <- ref.get
    } yield ids shouldEqual List(OrderId("1111"), OrderId("5555"))).unsafeRun()
  }

}
