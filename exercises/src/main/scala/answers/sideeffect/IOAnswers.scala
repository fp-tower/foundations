package answers.sideeffect

import java.time.Instant

import exercises.sideeffect.IORef

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.FiniteDuration
import scala.util.Try

object IOAnswers {

  /////////////////////////
  // 1. Smart constructors
  /////////////////////////

  object IO {
    def succeed[A](value: A): IO[A] =
      new IO(() => value)

    def pure[A](value: A): IO[A] =
      succeed(value)

    def fail[A](error: Throwable): IO[A] =
      new IO(() => throw error)

    val boom: IO[Nothing] = fail(new Exception("Boom!"))

    def effect[A](fa: => A): IO[A] =
      new IO(() => fa)

    // common alias for effect
    def apply[A](fa: => A): IO[A] =
      effect(fa)

    val notImplemented: IO[Nothing] = effect(???)

    def fromTry[A](fa: Try[A]): IO[A] =
      fa.fold(fail, succeed)

    def sleep(duration: FiniteDuration): IO[Unit] =
      effect(Thread.sleep(duration.toMillis))

    val forever: IO[Nothing] = notImplemented
  }

  /////////////////////
  // 2. IO API
  /////////////////////

  class IO[+A](val unsafeRun: () => A) {
    import IO._

    def map[B](f: A => B): IO[B] =
      effect(f(unsafeRun()))

    def flatMap[B](f: A => IO[B]): IO[B] =
      effect(f(unsafeRun()).unsafeRun())

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

    def attempt[B]: IO[Either[Throwable, A]] =
      map(Try(_).toEither)

    def retryOnce: IO[A] =
      attempt.flatMap {
        case Left(_)  => this
        case Right(a) => succeed(a)
      }

    def retryUntilSuccess(waitBeforeRetry: FiniteDuration): IO[A] =
      attempt.flatMap {
        case Left(_)  => sleep(waitBeforeRetry) *> retryUntilSuccess(waitBeforeRetry)
        case Right(a) => succeed(a)
      }
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

    val readInt: IO[Int] = readLine.map(parseInt).flatMap(IO.fromTry)
  }

  val stdin: Console = new Console {
    val readLine: IO[String]                 = IO.effect(scala.io.StdIn.readLine())
    def writeLine(message: String): IO[Unit] = IO.effect(println(message))
  }

  def testConsole(in: List[String], out: ListBuffer[String]): Console =
    new Console {
      var inRemaining: List[String] = in

      val readLine: IO[String] = inRemaining match {
        case Nil     => new IO(() => "")
        case x :: xs => inRemaining = xs; new IO(() => x)
      }

      def writeLine(message: String): IO[Unit] = {
        out += message
        new IO(() => ())
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
    TestConsole(IORef(in))

  case class TestConsole(in: IORef[List[String]]) extends Console {
    val out: IORef[List[String]] = IORef(List.empty[String])

    val readLine: IO[String] =
      in.modifyFold {
        case x :: xs => (x, xs)
        case Nil     => ("", Nil)
      }

    def writeLine(message: String): IO[Unit] =
      out.modify(message :: _).map(_ => ())
  }

}
