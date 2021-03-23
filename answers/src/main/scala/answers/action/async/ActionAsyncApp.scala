package answers.action.async

import answers.dataprocessing.ThreadPoolUtil

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object ActionAsyncApp extends App {
  val ec = ThreadPoolUtil.fixedSizeExecutionContext(4, "pool")

  Examples.parMany(ec).execute()
}

object Examples {
  def task(suffix: String, length: Int, duration: FiniteDuration): Action[Unit] =
    Action
      .sequence(
        List
          .range(0, length)
          .map(i => Action.log(s"Task $suffix $i") *> Action.sleep(duration))
      )
      .void

  def timeoutSucceed(ec: ExecutionContext): Action[Unit] =
    task("", 20, 200.milliseconds) // 4 seconds
      .timeout(10.second)(ec)

  def timeoutFailed(ec: ExecutionContext): Action[Unit] =
    task("", 20, 200.milliseconds) // 4 seconds
      .timeout(2.second)(ec)

  def par2(ec: ExecutionContext): Action[Unit] = {
    val taskA = task("A", 10, 200.milliseconds)
    val taskB = task("B", 8, 300.milliseconds)

    taskA.parZip(taskB)(ec).void
  }

  def parMany(ec: ExecutionContext): Action[Unit] = {
    val taskA = task("A", 20, 50.milliseconds)
    val taskB = task("B", 15, 100.milliseconds)
    val taskC = task("C", 10, 200.milliseconds)
    val taskD = task("D", 7, 300.milliseconds)

    Action.parSequence(List(taskA, taskB, taskC, taskD))(ec).void
  }
}
