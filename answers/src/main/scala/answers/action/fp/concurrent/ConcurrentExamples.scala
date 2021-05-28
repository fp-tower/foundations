package answers.action.fp.concurrent

import answers.action.fp._
import answers.dataprocessing.ThreadPoolUtil

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.{DurationInt, FiniteDuration}

object ConcurrentExamplesApp extends App {
  import ConcurrentExamples._

  val x = IO(1)
  println(x.toString)

  val boundedEC = ThreadPoolUtil.fixedSizeExecutionContext(4, "pool")

  parMany(boundedEC).unsafeRun()
}

object ConcurrentExamples {

  def parTwo(ec: ExecutionContext) = {
    val streamA = stream("A", 10, 200.millis)
    val streamB = stream("B", 10, 400.millis)

    streamA.parZip(streamB)(ec)
  }

  def parMany(ec: ExecutionContext) = {
    val streamA = stream("A", 2, 1000.millis)
    val streamB = stream("B", 5, 500.millis)
    val streamC = stream("C", 7, 300.millis)
    val streamD = stream("D", 10, 200.millis)
    val streamE = stream("E", 15, 100.millis)
    val streamF = stream("F", 20, 50.millis)

    List(streamA, streamB, streamC, streamD, streamE, streamF).parSequence(ec)
  }

  // Print "Task $taskName 0"
  // sleep $duration
  // Print "Task $taskName 1"
  // sleep $duration
  // ...
  // repeat $iteration times
  def stream(taskName: String, iteration: Int, duration: FiniteDuration) =
    List.range(0, iteration).traverse { n =>
      IO.debug(s"Task $taskName $n") *> IO.sleep(duration)
    }
}
