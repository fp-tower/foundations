package answers.sideeffect

import java.time.Instant

import cats.Monad
import exercises.sideeffect.IORef

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._
import scala.util.Try

object IOAnswersApp extends App {
  import IOAnswers._

  userConsoleProgram2(stdinConsole, systemClock).map(_.toString).flatMap(writeLine).unsafeRun()
}

object IOAnswers {

  /////////////////////////
  // 1. Smart constructors
  /////////////////////////

  object IO {
    def succeed[A](value: A): IO[A] =
      new IO[A] {
        def unsafeRun(): A = value
      }

    def pure[A](value: A): IO[A] =
      succeed(value)

    def fail[A](error: Throwable): IO[A] =
      new IO[A] {
        def unsafeRun(): A = throw error
      }

    val boom: IO[Nothing] = fail(new Exception("Boom!"))

    def effect[A](fa: => A): IO[A] =
      new IO[A] {
        def unsafeRun(): A = fa
      }

    // common alias for effect
    def apply[A](fa: => A): IO[A] =
      effect(fa)

    val unit: IO[Unit] = succeed(())

    val notImplemented: IO[Nothing] = effect(???)

    def fromTry[A](fa: Try[A]): IO[A] =
      fa.fold(fail, succeed)

    def sleep(duration: FiniteDuration): IO[Unit] =
      effect(Thread.sleep(duration.toMillis))

    val never: IO[Nothing] =
      effect {
        sleep(100.day).unsafeRun()
        never.unsafeRun()
      }

    implicit val monad: Monad[IO] = new Monad[IO] {
      def pure[A](x: A): IO[A]                           = IO.pure(x)
      def flatMap[A, B](fa: IO[A])(f: A => IO[B]): IO[B] = fa.flatMap(f)
      def tailRecM[A, B](a: A)(f: A => IO[Either[A, B]]): IO[B] = f(a).flatMap {
        case Left(a2) => tailRecM(a2)(f) // not tailrec
        case Right(b) => pure(b)
      }
    }
  }

  /////////////////////
  // 2. IO API
  /////////////////////

  trait IO[+A] { self =>
    import IO._

    def unsafeRun(): A

    def map[B](f: A => B): IO[B] =
      effect(f(unsafeRun()))

    def flatMap[B](f: A => IO[B]): IO[B] =
      effect(f(unsafeRun()).unsafeRun())

    def void: IO[Unit] = map(_ => ())

    def tuple2[B](fb: IO[B]): IO[(A, B)] =
      for {
        a <- this
        b <- fb
      } yield (a, b)

    def productL[B](fb: IO[B]): IO[A] =
      tuple2(fb).map(_._1)

    def productR[B](fb: IO[B]): IO[B] =
      tuple2(fb).map(_._2)

    // common alias for productL
    def <*[B](fb: IO[B]): IO[A] = productL(fb)

    // common alias for productR
    def *>[B](fb: IO[B]): IO[B] = productR(fb)

    def attempt: IO[Try[A]] =
      effect(Try(unsafeRun()))

    def handleErrorWith[B >: A](f: Throwable => IO[B]): IO[B] =
      attempt.flatMap(_.fold(f, succeed))

    def retryOnce: IO[A] =
      handleErrorWith(_ => this)

    def retryUntilSuccess(waitBeforeRetry: FiniteDuration): IO[A] =
      handleErrorWith(_ => sleep(waitBeforeRetry) *> retryUntilSuccess(waitBeforeRetry))
  }

  ////////////////////
  // 3. Programs
  ////////////////////

  def unsafeReadLine: String =
    scala.io.StdIn.readLine()

  def unsafeWriteLine(message: String): Unit =
    println(message)

  val readLine: IO[String] =
    IO.effect(scala.io.StdIn.readLine())

  def writeLine(message: String): IO[Unit] =
    IO.effect(println(message))

  def unsafeConsoleProgram: String = {
    println("What's your name?")
    val name = scala.io.StdIn.readLine()
    println(s"Your name is $name")
    name
  }

  val consoleProgram: IO[String] =
    for {
      _    <- writeLine("What's your name?")
      name <- readLine
      _    <- writeLine(s"Your name is $name")
    } yield name

  def parseInt(x: String): Try[Int] = Try(x.toInt)

  val readInt: IO[Int] = readLine.map(parseInt).flatMap(IO.fromTry)

  case class User(name: String, age: Int, createdAt: Instant)

  val readNow: IO[Instant] = IO.effect(Instant.now())

  val userConsoleProgram: IO[User] =
    for {
      _         <- writeLine("What's your name?")
      name      <- readLine
      _         <- writeLine("What's your age?")
      age       <- readInt
      createdAt <- readNow
    } yield User(name, age, createdAt)

  def unsafeUserConsoleProgram: User = {
    println("What's your name?")
    val name = scala.io.StdIn.readLine()
    println("What's your age?")
    val age = scala.io.StdIn.readLine().toInt
    User(name, age, createdAt = Instant.now())
  }

  ////////////////////////
  // 5. Testing
  ////////////////////////

  trait Clock {
    val readNow: IO[Instant]
  }

  val systemClock: Clock = new Clock {
    val readNow: IO[Instant] = IO.effect(Instant.now())
  }

  def testClock(constant: Instant): Clock = new Clock {
    val readNow: IO[Instant] = IO.succeed(constant)
  }

  trait Console {
    val readLine: IO[String]
    def writeLine(message: String): IO[Unit]

    def readInt: IO[Int] = readLine.map(parseInt).flatMap(IO.fromTry)
  }

  val stdinConsole: Console = new Console {
    val readLine: IO[String]                 = IO.effect(scala.io.StdIn.readLine())
    def writeLine(message: String): IO[Unit] = IO.effect(println(message))
  }

  def testConsole(in: List[String], out: ListBuffer[String]): Console =
    new Console {
      var inRemaining: List[String] = in

      val readLine: IO[String] = inRemaining match {
        case Nil     => IO.succeed("")
        case x :: xs => inRemaining = xs; IO.succeed(x)
      }

      def writeLine(message: String): IO[Unit] = {
        out += message
        IO.succeed(())
      }
    }

  def userConsoleProgram2(console: Console, clock: Clock): IO[User] =
    for {
      _         <- console.writeLine("What's your name?")
      name      <- console.readLine
      _         <- console.writeLine("What's your age?")
      age       <- console.readInt
      createdAt <- clock.readNow
    } yield User(name, age, createdAt)

  def safeTestConsole(in: List[String]): TestConsole =
    TestConsole(IORef.unsafe(in))

  case class TestConsole(in: IORef[List[String]]) extends Console {
    val out: IORef[List[String]] = IORef.unsafe(List.empty[String])

    val readLine: IO[String] =
      in.modify {
        case x :: xs => (xs, x)
        case Nil     => (Nil, "")
      }

    def writeLine(message: String): IO[Unit] =
      out.update(_ :+ message)
  }

  ////////////////////////
  // 5. Advanced API
  ////////////////////////

  def sequence[A](xs: List[IO[A]]): IO[List[A]] =
    IO.effect(
      xs.map(_.unsafeRun())
    )

  def traverse[A, B](xs: List[A])(f: A => IO[B]): IO[List[B]] =
    sequence(xs.map(f))

  case class UserId(value: String)
  case class OrderId(value: String)

  case class User_V2(userId: UserId, name: String, orderIds: List[OrderId])

  trait UserOrderApi {
    def getUser(userId: UserId): IO[User_V2]
    def deleteOrder(orderId: OrderId): IO[Unit]
  }

  def deleteAllUserOrders(api: UserOrderApi)(userId: UserId): IO[Unit] =
    for {
      user <- api.getUser(userId)
      _    <- traverse(user.orderIds)(api.deleteOrder)
    } yield ()

}
