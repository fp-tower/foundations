package answers.sideeffect

import exercises.sideeffect.{IO, IORef}
import exercises.sideeffect.IOExercises.{parseInt, Console, User}
import toimpl.sideeffect.IOToImpl

import scala.collection.mutable.ListBuffer

object IOAnswers extends IOToImpl {

  val readLine: IO[String] =
    new IO(() => scala.io.StdIn.readLine())

  def writeLine(message: String): IO[Unit] =
    new IO(() => println(message))

  val consoleProgram: IO[String] = new IO(() => {
    writeLine("What's your name?").unsafeRun()
    val name = readLine.unsafeRun()
    writeLine("Your name is $name").unsafeRun()
    name
  })

  def map[A, B](fa: IO[A])(f: A => B): IO[B] =
    new IO(() => f(fa.unsafeRun()))

  val readLength: IO[Int] =
    readLine.map(_.length)

  val readInt: IO[Int] =
    readLine.map(x => parseInt(x).getOrElse(throw new Exception(s"Invalid Int $x")))

  val userConsoleProgram: IO[User] = new IO(() => {
    writeLine("What's your name?").unsafeRun()
    val name = readLine.unsafeRun()
    writeLine("What's your age?").unsafeRun()
    val age = readInt.unsafeRun()
    User(name, age)
  })

  def map2[A, B, C](fa: IO[A], fb: IO[B])(f: (A, B) => C): IO[C] =
    new IO(() => {
      val a = fa.unsafeRun()
      val b = fb.unsafeRun()
      f(a, b)
    })

  def map4[A, B, C, D, E](fa: IO[A], fb: IO[B], fc: IO[C], fd: IO[D])(f: (A, B, C, D) => E): IO[E] =
    map2(
      map2(fa, fb)((a, b) => (a, b)), // IO[(A, B)]
      map2(fc, fd)((c, d) => (c, d)) // IO[(C, D)]
    ) { case ((a, b), (c, d)) => f(a, b, c, d) }

  val consoleProgram2: IO[String] =
    map2(writeLine("What's your name?"), readLine)((_, name) => name)

  val userConsoleProgram2: IO[User] =
    map4(writeLine("What's your name?"), readLine, writeLine("What's your age?"), readInt)(
      (_, name, _, age) => User(name, age)
    )

  def flatMap[A, B](fa: IO[A])(f: A => IO[B]): IO[B] =
    new IO(() => {
      val a = fa.unsafeRun()
      f(a).unsafeRun()
    })

  val userConsoleProgram3: IO[User] =
    for {
      _    <- writeLine("What's your name?")
      name <- readLine
      _    <- writeLine("What's your age?")
      age  <- readInt
    } yield User(name, age)

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

  def userConsoleProgram4(console: Console): IO[User] =
    for {
      _    <- console.writeLine("What's your name?")
      name <- console.readLine
      _    <- console.writeLine("What's your age?")
      age  <- console.readInt
    } yield User(name, age)

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
