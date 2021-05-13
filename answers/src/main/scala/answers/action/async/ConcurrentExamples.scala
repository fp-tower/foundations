package answers.action.async

import answers.dataprocessing.ThreadPoolUtil

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

object ConcurrentExamplesApp extends App {
  import ConcurrentExamples._

  val ec = ThreadPoolUtil.fixedSizeExecutionContext(4, "pool")

  parMany(ec).unsafeRun()
}

object ConcurrentExamples {

  def parTwo(ec: ExecutionContext): IO[Any] = {
    val streamA = stream("A", 10, 200.millis)
    val streamB = stream("B", 8, 400.millis)

    streamA.parZip(streamB)(ec)
  }

  def parMany(ec: ExecutionContext): IO[Any] = {
    val streamA = stream("A", 2, 1000.millis)
    val streamB = stream("B", 5, 500.millis)
    val streamC = stream("C", 7, 300.millis)
    val streamD = stream("D", 10, 200.millis)
    val streamE = stream("E", 15, 100.millis)
    val streamF = stream("F", 20, 50.millis)

    List(streamA, streamB, streamC, streamD, streamE, streamF).parSequence(ec)
  }

  def timeoutSucceed(ec: ExecutionContext): IO[Any] =
    stream("", 20, 200.millis) // 4 seconds
      .timeout(10.seconds)(ec)

  def timeoutFailed(ec: ExecutionContext): IO[Any] =
    stream("", 20, 200.millis) // 4 seconds
      .timeout(2.seconds)(ec)

  // Print "Task $taskName 0"
  // sleep $duration
  // Print "Task $taskName 1"
  // sleep $duration
  // ...
  // repeat $iteration times
  def stream(taskName: String, iteration: Int, duration: FiniteDuration): IO[Any] =
    List.range(0, iteration).traverse { n =>
      IO.debug(s"Task $taskName $n") *> IO.sleep(duration)
    }
}
