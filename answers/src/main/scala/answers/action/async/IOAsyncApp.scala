package answers.action.async

import answers.dataprocessing.ThreadPoolUtil

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object IOAsyncApp extends App {

  val ec = ThreadPoolUtil.fixedSizeExecutionContext(4, "pool")

  ParExec.run(ec)

}

object Race {
  val taskA: IO[Unit] = (IO.log("Task A") *> IO.sleep(200.milliseconds)).repeat(20)
  val taskB: IO[Unit] = IO.log("Task B") *> IO.sleep(1.second) *> IO.fail[Unit](new Exception("Boom"))

  def run(ec: ExecutionContext): Either[Unit, Unit] =
    taskA.race(taskB)(ec).execute()
}

object ParExec {
  val taskA: IO[Unit] = (IO.log("Task A") *> IO.sleep(200.milliseconds)).repeat(10)
  val taskB: IO[Unit] = (IO.log("Task B") *> IO.sleep(300.milliseconds)).repeat(8)

  def run(ec: ExecutionContext): Unit =
    taskA.parMap2(taskB)((_, _) => ())(ec).execute()
}
