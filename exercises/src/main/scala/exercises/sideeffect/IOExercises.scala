package exercises.sideeffect

import java.time.Instant

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

object IOExercisesApp extends App {
  import IOExercises._

  unsafeConsoleProgram
}

object IOExercises {

  /////////////////////////
  // 1. Smart constructors
  /////////////////////////

  object IO {
    // 1a. Implement `succeed` a smart constructor that lifts a pure value into an IO
    // such as succeed(x).unsafeRun() == x
    // Note that `succeed` is strict, it means the same value will be returned every time it is run.
    // It also means it is unsafe to throw an Exception when you call `succeed`, e.g. succeed(throw new Exception("")).
    def succeed[A](value: A): IO[A] =
      new IO[A] {
        def unsafeRun(): A = ???
      }

    // common alias for `succeed`
    // IO.pure(4) == IO.succeed(4)
    def pure[A](value: A): IO[A] =
      succeed(value)

    // 1b. Implement `fail` a smart constructor that creates a failing IO.
    // Note that the following code does not throw:
    // val error = fail(new Exception(""))
    // The Exception is only thrown when unsafeRun is called, e.g. error.unsafeRun().
    def fail[A](error: Throwable): IO[A] =
      new IO[A] {
        def unsafeRun(): A = ???
      }

    // 1c. Write a test for `fail` in IOExercisesTest

    // 1d. What is the type of `boom`? Try to guess without using your IDE.
    val boom = fail(new Exception("Boom!"))

    def fromTry[A](fa: Try[A]): IO[A] =
      fa.fold(fail, succeed)

    // 1e. Implement `effect` that imports a block of code with a potential side effect into an IO
    // such as effect(4) == succeed(4)
    // and     effect(throw new Exception("")) == fail(new Exception(""))
    // Use case:
    // effect(println("hello"))
    // effect(http.delete("http://foo.com/order/1234"))
    // What's the difference between `effect` and `succeed`?
    def effect[A](block: => A): IO[A] =
      new IO[A] {
        def unsafeRun(): A = ???
      }

    // common alias for `effect`
    // IO { println("hello") } instead of effect(println("hello"))
    def apply[A](fa: => A): IO[A] =
      effect(fa)

    def notImplemented[A]: IO[A] =
      fail(new NotImplementedError)

    // 1f. Implement `sleep`, see Thread.sleep
    // What is the issue with this implementation?
    def sleep(duration: FiniteDuration): IO[Unit] =
      ???

    // 1g. Implement `never` an IO that never completes
    // This should be an equivalent to`sleep` with an Infinite duration.
    // Why `never` has a different return type than `sleep`?
    val never: IO[Nothing] =
      new IO[Nothing] {
        def unsafeRun(): Nothing = ???
      }
  }

  /////////////////////
  // 2. IO API
  /////////////////////

  trait IO[A] { self =>
    import IO._

    def unsafeRun(): A

    // 2a. Implement `map`
    // such as succeed(x).map(f) == succeed(f(x))
    // and     fail(e).map(f) == fail(e)
    // Note that f is a pure function, you should NOT use it to do another side effect e.g. succeed(4).map(println).
    def map[B](f: A => B): IO[B] =
      ???

    // `void` discards the return value.
    // Use case:
    // val rowsUpdated: IO[Int] = updateDb(sql"...")
    // val response: IO[Unit] = rowsUpdated.void
    def void: IO[Unit] =
      map(_ => ())

    // 2b. Implement `flatMap`
    // such as succeed(x).flatMap(f) == f(x)
    // and     fail(e).flatMap(f) == fail(e)
    def flatMap[B](f: A => IO[B]): IO[B] =
      ???

    // `productL` and `productR` combines independent IO where one of them is only used for its side effect.
    // This is a rare case where an infix operator is really convenient see `<*` and `*>`
    // Use case:
    // logInfo("Fetching user $userId") *> getUser($userId)            : IO[User]  // logInfo value is discarded
    // CreateOrder(item, qty, userId) <* sendConfirmationEmail(userId) : IO[Order] // sendConfirmationEmail value is discarded.
    // Note that we can use a for comprehension because we already implemented `map` and `flatMap`.
    def productL[B](fb: IO[B]): IO[A] =
      for {
        a <- this
        _ <- fb
      } yield a

    def productR[B](fb: IO[B]): IO[B] =
      for {
        _ <- this
        b <- fb
      } yield b

    // common alias for `productL`
    def <*[B](fb: IO[B]): IO[A] =
      productL(fb)

    // common alias for `productR`
    def *>[B](fb: IO[B]): IO[B] =
      productR(fb)

    // 2c. Implement `attempt` which makes the error part of IO explicit.
    // Try[A] is either a Success(a: A) or a Failure(e: Throwable)
    // such as succeed(x).attempt == succeed(Success(x))
    //         fail(new Exception("")).attempt == succeed(Failure(new Exception(""))).
    // Note that `attempt` guarantees that unsafeRun() will not throw an Exception.
    def attempt: IO[Try[A]] = ???

    // 2d. Implement `handleErrorWith` which allows to catch failing IO
    // such as fail(new Exception("")).handleErrorWith(_ => someIO) == someIO
    //         fail(new Exception("foo")).handleErrorWith{
    //            case e: IllegalArgumentException => succeed(1)
    //            case other                       => succeed(2)
    //         } == succeed(2)
    // Use case:
    // handleErrorWith(e => IO.effect(log.error("Operation failed"), e))
    def handleErrorWith(f: Throwable => IO[A]): IO[A] = ???

    // 2e. Implement `retryOnce` which re-runs the current IO if it fails.
    def retryOnce: IO[A] = ???

    // 2f. Implement `retryUntilSuccess`
    // similar to `retryOnce` but it retries until the IO succeeds (potentially indefinitely)
    // sleep `waitBeforeRetry` between each retry
    // How would you update this method to implement an exponential backoff?
    def retryUntilSuccess(waitBeforeRetry: FiniteDuration): IO[A] = ???
  }

  ////////////////////
  // 3. Programs
  ////////////////////

  def unsafeReadLine: String =
    scala.io.StdIn.readLine()

  def unsafeWriteLine(message: String): Unit =
    println(message)

  // 3a. Implement `readLine` and `writeLine` such as it is a pure version of `unsafeReadLine` and `unsafeWriteLine`
  // Which smart constructor of IO should you use?
  val readLine: IO[String] =
    IO.notImplemented

  def writeLine(message: String): IO[Unit] =
    IO.notImplemented

  def unsafeConsoleProgram: String = {
    println("What's your name?")
    val name = scala.io.StdIn.readLine()
    println(s"Your name is $name")
    name
  }

  // 3b. Implement `consoleProgram` such as it is a pure version of `unsafeConsoleProgram`.
  // Try to re-use `readLine`, `writeLine` and IO combinators.
  val consoleProgram: IO[String] =
    IO.notImplemented

  // 3c. Implement `readInt` which reads an Int from the command line
  // such as readInt.unsafeRun() == 32 if user types "32"
  // and     readInt.unsafeRun() throws an Exception if user types "hello"
  // use `parseInt` and `readLine`
  def parseInt(x: String): Try[Int] =
    Try(x.toInt)

  val readInt: IO[Int] =
    IO.notImplemented

  // 3d. Implement `userConsoleProgram` such as it is a pure version of `unsafeUserConsoleProgram`
  case class User(name: String, age: Int, createdAt: Instant)

  val readNow: IO[Instant] = IO.effect(Instant.now())

  val userConsoleProgram: IO[User] =
    IO.notImplemented

  def unsafeUserConsoleProgram: User = {
    println("What's your name?")
    val name = scala.io.StdIn.readLine()
    println("What's your age?")
    val age = scala.io.StdIn.readLine().toInt
    User(name, age, createdAt = Instant.now())
  }

  // 3e. How would you test `userConsoleProgram`?
  // What are the issues with the current implementation?

  ////////////////////////
  // 4. Testing
  ////////////////////////

  trait Clock {
    val readNow: IO[Instant]
  }

  val systemClock: Clock = new Clock {
    val readNow: IO[Instant] = IO.effect(Instant.now())
  }

  // 4a. Implement `testClock` which facilitates testing of a Clock API.
  def testClock: Clock = ???

  trait Console {
    val readLine: IO[String]
    def writeLine(message: String): IO[Unit]

    def readInt: IO[Int] = readLine.map(parseInt).flatMap(IO.fromTry)
  }

  val stdinConsole: Console = new Console {
    val readLine: IO[String]                 = IO.effect(scala.io.StdIn.readLine())
    def writeLine(message: String): IO[Unit] = IO.effect(println(message))
  }

  // 4b. Implement `testConsole` which facilitates testing of a Console API.
  // Use both `testClock` and `testConsole` to write a test for `userConsoleProgram2` in IOExercisesTest
  def testConsole(in: List[String], out: ListBuffer[String]): Console = ???

  def userConsoleProgram2(console: Console, clock: Clock): IO[User] =
    for {
      _         <- console.writeLine("What's your name?")
      name      <- console.readLine
      _         <- console.writeLine("What's your age?")
      age       <- console.readInt
      createdAt <- clock.readNow
    } yield User(name, age, createdAt)

  // 4c. Now our production code is "pure" (free of side effect) but our test code is not.
  // How would you fix this?
  // Try to implement `safeTestConsole` such as:
  // - it is a pure Console implementation
  // - it makes unit testing convenient
  def safeTestConsole: Console = ???

  ////////////////////////
  // 5. Advanced API
  ////////////////////////

  // 5a. Implement `sequence` which runs sequentially a list of IO and collects the results.
  // sequence(List(succeed(1), succeed(2))) == succeed(List(1,2))
  // sequence(List(succeed(1), notImplemented, succeed(3))) == succeed(1) *> notImplemented
  // use case:
  // val userIds: List[UserId] = ...
  // sequence(userIds.map(fetchUser)): IO[List[User]]
  def sequence[A](xs: List[IO[A]]): IO[List[A]] =
    IO.notImplemented

  // 5b. Implement `traverse`
  // traverse(List(1,2,3))(succeed) == succeed(List(1,2,3))
  // traverse(List(1,2,3))(x => if(x > 1) notImplemented else succeed(x)) == succeed(1) *> notImplemented
  // use case:
  // val userIds: List[UserId] = ...
  // userIds.traverse(fetchUser): IO[List[User]]
  def traverse[A, B](xs: List[A])(f: A => IO[B]): IO[List[B]] =
    IO.notImplemented

  // 5c. Implement `deleteAllUserOrders` such as it fetches a user: User_V2 and delete all associated orders
  // e.g. if `getUser` returns User_V2(UserId("1234"), "Rob", List(OrderId("1111"), OrderId("5555")))
  //      Then we would call deleteOrder(OrderId("1111")) and deleteOrder(OrderId("5555")).
  // Try to use `sequence` or `traverse`.
  case class UserId(value: String)
  case class OrderId(value: String)

  case class User_V2(userId: UserId, name: String, orderIds: List[OrderId])

  trait UserOrderApi {
    def getUser(userId: UserId): IO[User_V2]
    def deleteOrder(orderId: OrderId): IO[Unit]
  }

  def deleteTwoOrders(api: UserOrderApi)(orderId1: OrderId, orderId2: OrderId): IO[Unit] =
    ???

  def deleteAllUserOrders(api: UserOrderApi)(userId: UserId): IO[Unit] =
    ???

}
