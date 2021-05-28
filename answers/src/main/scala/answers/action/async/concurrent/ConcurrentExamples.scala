package answers.action.async.concurrent

import answers.action.async._
import answers.dataprocessing.ThreadPoolUtil

import scala.concurrent.duration._

object ConcurrentExamples extends App {

  val ec = ThreadPoolUtil.fixedSizeExecutionContext(4, "pool")

  val streamA = stream("A", 2, 1000.millis)
  val streamB = stream("B", 5, 500.millis)
  val streamC = stream("C", 7, 300.millis)
  val streamD = stream("D", 10, 200.millis)
  val streamE = stream("E", 15, 100.millis)
  val streamF = stream("F", 20, 50.millis)

  val seqTwo         = streamA.zip(streamB)
  val parTwo         = streamA.parZip(streamB)(ec)
  val parMany        = List(streamA, streamB, streamC, streamD, streamE, streamF).parSequence2(ec)
  val timeoutSucceed = streamD.timeout(10.seconds)(ec)
  val timeoutFailed  = streamD.timeout(500.millis)(ec)

  parMany.unsafeRun()

  // Print "Task $taskName 0"
  // sleep $duration
  // Print "Task $taskName 1"
  // sleep $duration
  // ...
  // repeat $iteration times
  def stream(taskName: String, iteration: Int, duration: FiniteDuration): IO[Any] =
    List.range(0, iteration).traverse { n =>
      (IO.debug(s"Task $taskName $n") andThen IO.sleep(duration)).evalOn(ec)
    }
}
