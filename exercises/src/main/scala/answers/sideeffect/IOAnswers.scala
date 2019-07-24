package answers.sideeffect

import exercises.sideeffect.{IO, IORef}
import exercises.sideeffect.IOExercises.{map4, parseInt, readLine, writeLine, Console, User}
import toimpl.sideeffect.IOToImpl

import scala.collection.mutable.ListBuffer

object IOAnswers extends IOToImpl {

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

  def safeTestConsole(in: IORef[List[String]]): TestConsole =
    TestConsole(in)

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
