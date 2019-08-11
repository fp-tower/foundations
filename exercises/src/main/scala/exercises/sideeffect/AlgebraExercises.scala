package exercises.sideeffect

import answers.sideeffect.IOAnswers.IO
import cats.free.Free
import cats.~>

object AlgebraExercises {

  sealed trait Action[A]
  object Action {
    case class Log(level: LogLevel, message: String)    extends Action[Unit]
    case class ReadFile(name: String)                   extends Action[List[String]]
    case class WriteFile(name: String, content: String) extends Action[Unit]
    case object RandomInt                               extends Action[Int]
  }

  sealed trait LogLevel
  object LogLevel {
    case object Info  extends LogLevel
    case object Error extends LogLevel
  }

  type ActionF[A] = Free[Action, A]

  import Action._

  def log(level: LogLevel, message: String): ActionF[Unit]    = Free.liftF(Log(level, message))
  def logInfo(message: String): ActionF[Unit]                 = Free.liftF(Log(LogLevel.Info, message))
  def logError(message: String): ActionF[Unit]                = Free.liftF(Log(LogLevel.Error, message))
  def readFile(name: String): ActionF[List[String]]           = Free.liftF(ReadFile(name))
  def writeFile(name: String, content: String): ActionF[Unit] = Free.liftF(WriteFile(name, content))
  val randomInt: ActionF[Int]                                 = Free.liftF(RandomInt)

  def wordCount(fileName: String): ActionF[Int] =
    for {
      lines <- readFile(fileName)
      _     <- logInfo(s"Read ${lines.length} lines from $fileName")
      words = lines.flatMap(splitWords)
      count = words.size
      _ <- logInfo(s"Counted $count words")
    } yield count

  def splitWords(line: String): List[String] =
    line.split(" ").toList

  val interpreter: Action ~> IO = new (Action ~> IO) {
    def apply[A](fa: Action[A]): IO[A] =
      fa match {
        case Log(level, message)      => IO.effect(println(s"[$level] $message"))
        case ReadFile(name)           => IO.effect(scala.io.Source.fromResource(name).getLines.toList)
        case WriteFile(name, content) => IO.notImplemented
        case RandomInt                => IO.effect(scala.util.Random.nextInt())
      }
  }

  def onlyErrorLog(nat: Action ~> IO): Action ~> IO = new (Action ~> IO) {
    def apply[A](fa: Action[A]): IO[A] =
      fa match {
        case Log(LogLevel.Info, _) => IO.unit
        case _                     => nat(fa)
      }
  }

}

object AlgebraMain extends IOApp {
  import AlgebraExercises._

  def main(): IO[Unit] =
    wordCount("50-word-count.txt")
      .foldMap(interpreter)
      .map(println)
      .void
}
