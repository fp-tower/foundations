package answers.action.async

import answers.dataprocessing.ThreadPoolUtil

import java.time.Duration
import scala.concurrent.ExecutionContext

object ActionAsyncApp extends App {
  import Examples._

  val ec = ThreadPoolUtil.fixedSizeExecutionContext(4, "pool")

  parTwo(ec).unsafeRun()
}

object Examples {

  def parTwo(ec: ExecutionContext): IO[Any] = {
    val taskA = stream("A", 10, Duration.ofMillis(200))
    val taskB = stream("B", 8, Duration.ofMillis(300))

    taskA.parZip(taskB)(ec)
  }

  def parMany(ec: ExecutionContext): IO[Any] = {
    val taskA = stream("A", 20, Duration.ofMillis(50))
    val taskB = stream("B", 15, Duration.ofMillis(100))
    val taskC = stream("C", 10, Duration.ofMillis(200))
    val taskD = stream("D", 7, Duration.ofMillis(300))

    IO.parSequence(List(taskA, taskB, taskC, taskD))(ec)
  }

  def timeoutSucceed(ec: ExecutionContext): IO[Any] =
    stream("", 20, Duration.ofMillis(200)) // 4 seconds
      .timeout(Duration.ofSeconds(10))(ec)

  def timeoutFailed(ec: ExecutionContext): IO[Any] =
    stream("", 20, Duration.ofMillis(200)) // 4 seconds
      .timeout(Duration.ofSeconds(2))(ec)

  // Print "Task $suffix 0"
  // sleep $duration
  // Print "Task $suffix 1"
  // sleep $duration
  // ...
  // repeat $iteration times
  def stream(suffix: String, iteration: Int, duration: Duration): IO[Any] =
    List.range(0, iteration).traverse { n =>
      IO.debug(s"Task $suffix $n") *> IO.sleep(duration)
    }
}
