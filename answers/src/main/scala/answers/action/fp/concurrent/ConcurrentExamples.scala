package answers.action.fp.concurrent

import answers.action.fp._
import answers.dataprocessing.ThreadPoolUtil

import java.time.Duration
import scala.concurrent.ExecutionContext

object ConcurrentExamplesApp extends App {
  import ConcurrentExamples._

  val boundedEC = ThreadPoolUtil.fixedSizeExecutionContext(8, "pool")

  parTwo(boundedEC).unsafeRun()
}

object ConcurrentExamples {

  def parTwo(ec: ExecutionContext) = {
    val streamA = stream("A", 10, Duration.ofMillis(200))
    val streamB = stream("B", 10, Duration.ofMillis(400))

    streamA.parZip(streamB)(ec)
  }

  def parMany(ec: ExecutionContext) = {
    val streamA = stream("A", 2, Duration.ofMillis(1000))
    val streamB = stream("B", 5, Duration.ofMillis(500))
    val streamC = stream("C", 7, Duration.ofMillis(300))
    val streamD = stream("D", 10, Duration.ofMillis(200))
    val streamE = stream("E", 15, Duration.ofMillis(100))
    val streamF = stream("F", 20, Duration.ofMillis(50))

    List(streamA, streamB, streamC, streamD, streamE, streamF).parSequence(ec)
  }

  // Print "Task $suffix 0"
  // sleep $duration
  // Print "Task $suffix 1"
  // sleep $duration
  // ...
  // repeat $iteration times
  def stream(suffix: String, iteration: Int, duration: Duration) =
    List.range(0, iteration).traverse { n =>
      IO.debug(s"Task $suffix $n") *> IO.sleep(duration)
    }
}
