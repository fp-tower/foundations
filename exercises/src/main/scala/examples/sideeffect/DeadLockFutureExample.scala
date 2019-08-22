package examples.sideeffect

import java.util.concurrent.Executors

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, ExecutionContextExecutor, Future}

object DeadLockFutureExample extends App {
  import DeadLockFuture._

  val ec: ExecutionContextExecutor = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1))

  val multiplied = Await.result(multiply(4, 5)(ec), Duration.Inf)

  println(s"multiplied: $multiplied")

}

object DeadLockFuture {

  // adapted from monix https://monix.io/docs/3x/best-practices/blocking.html
  def multiply(x: Int, y: Int)(ec: ExecutionContextExecutor): Future[Int] =
    Future {

      val fa = addOne(x)(ec)
      val fb = addOne(y)(ec)

      val result = fa.flatMap(a => fb.map(b => a * b)(ec))(ec)
      println(s"result $result")

      Await.result(result, Duration.Inf)
    }(ec)

  def addOne(x: Int)(ec: ExecutionContextExecutor): Future[Int] =
    Future {
      println(s"addOne $x")
      x + 1
    }(ec)

}
