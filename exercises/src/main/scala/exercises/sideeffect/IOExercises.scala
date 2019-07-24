package exercises.sideeffect

import toimpl.sideeffect.IOToImpl

import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Success, Try}

object IOExercises extends IOToImpl {

  val readLine: IO[String] =
    new IO(() => scala.io.StdIn.readLine())

  def writeLine(message: String): IO[Unit] =
    new IO(() => println(message))

  // 1. Implement consoleProgram such as it behaves similarly to unsafeConsoleProgram
  // Try to re-use readLine and writeLine
  // consoleProgram should only execute actions when the IO is run
  def unsafeConsoleProgram: String = {
    println("What's your name?")
    val name = scala.io.StdIn.readLine()
    println(s"Your name is $name")
    name
  }

  val consoleProgram: IO[String] = new IO(() => ???)

  // 2a. Implement map
  // such as map(readLine)(_.reverse) creates a IO that will return the reverse of the typed String
  def map[A, B](fa: IO[A])(f: A => B): IO[B] = ???

  // 2b. Implement map
  // such as readLength creates a IO that will return the length of the typed String
  // use map
  // Note: you can use map in infix or prefix position
  // val one: IO[Int] = _ => 1
  // one.map(_ + 1) or map(one)(_ + 1) (see Ops syntax in IOToImpl)
  val readLength: IO[Int] = new IO(() => ???)

  // 2c. Implement readInt using readLine
  // such as readInt will return an Int if the user typed a valid Int or throws an exception if it is invalid
  // use map and parseInt
  def parseInt(x: String): Try[Int] = Try(x.toInt)

  // Try[A] is either a Failure or a Success, you can pattern match on Try e.g.
  def tryToStatusCode[A](fa: Try[A]): Int =
    fa match {
      case Failure(_) => 0
      case Success(_) => 1
    }

  val readInt: IO[Int] = new IO(() => ???)

  // 3a. Implement consoleProgram2 such as it behaves similarly to userConsoleProgram
  case class User(name: String, age: Int)

  def unsafeUserConsoleProgram: User = {
    println("What's your name?")
    val name = scala.io.StdIn.readLine()
    println("What's your age?")
    val age = scala.io.StdIn.readLine().toInt
    User(name, age)
  }

  val userConsoleProgram: IO[User] = new IO(() => ???)

  // 3b. Implement map2 which combines independent thunks
  // such as map2(readLine, readLine)(_ ++ _) reads 2 lines from StdIn and concatenates result
  // Note: fa should be executed before fb when the resulting thunk is run
  def map2[A, B, C](fa: IO[A], fb: IO[B])(f: (A, B) => C): IO[C] = ???

  // 3c. Implement consoleProgram2 (same as consoleProgram) using map2
  // Note: you can use map2 in infix or prefix position
  // val one: IO[Int] = _ => 1
  // (one, one).map2(_ + _) or map2(one, one)(_ + _) (see Ops syntax in IOToImpl)
  val consoleProgram2: IO[String] = new IO(() => ???)

  // 3c. Implement userConsoleProgram2 (same as userConsoleProgram) using map4
  // Note: you can use map4 in infix or prefix position
  // val one: IO[Int] = _ => 1
  // (one, one, one, one).map4(_ + _ + _ + _) or map4(one, one, one, one)(_ + _ + _ + _) (see Ops syntax in IOToImpl)
  def map4[A, B, C, D, E](fa: IO[A], fb: IO[B], fc: IO[C], fd: IO[D])(f: (A, B, C, D) => E): IO[E] =
    map2(
      map2(fa, fb)((a, b) => (a, b)), // IO[(A, B)]
      map2(fc, fd)((c, d) => (c, d)) // IO[(C, D)]
    ) { case ((a, b), (c, d)) => f(a, b, c, d) }

  val userConsoleProgram2: IO[User] = new IO(() => ???)

  // 3d. Can you always sequence IOs using mapX? If no, find an example

  // 4a. Implement flatMap
  def flatMap[A, B](fa: IO[A])(f: A => IO[B]): IO[B] = ???

  // 4b. Implement userConsoleProgram3 (same as userConsoleProgram) using flatMap
  // Note: you can use map4 in infix or prefix position
  // val one: IO[Int] = _ => 1
  // def inc(x: Int): IO[Int] = _ => x + 1
  // one.flatMap(inc) or flatMap(one)(inc) (see Ops syntax in IOToImpl)
  val userConsoleProgram3: IO[User] = new IO(() => ???)

  ////////////////////////
  // 5. Testing
  ////////////////////////

  trait Console {
    val readLine: IO[String]
    def writeLine(message: String): IO[Unit]

    val readInt: IO[Int] =
      readLine.map(x => parseInt(x).getOrElse(throw new Exception(s"Invalid Int $x")))
  }

  val stdin: Console = new Console {
    val readLine: IO[String]                 = new IO(() => scala.io.StdIn.readLine())
    def writeLine(message: String): IO[Unit] = new IO(() => println(message))
  }

  // 5a. Implement a testConsole
  def testConsole(in: List[String], out: ListBuffer[String]): Console = ???

}
