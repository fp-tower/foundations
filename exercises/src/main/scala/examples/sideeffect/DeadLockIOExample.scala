package examples.sideeffect

import java.util.concurrent.Executors

import answers.sideeffect.IOAsync

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, ExecutionContextExecutor, Future}

object DeadLockIOExample extends App {
  import DeadLockIO._

  val ec: ExecutionContextExecutor = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1))

  val multiplied = multiply(4, 5)(ec).unsafeRun()

  println(s"multiplied: $multiplied")

}

object DeadLockIO {

  // adapted from monix https://monix.io/docs/3x/best-practices/blocking.html
  def multiply(x: Int, y: Int)(ec: ExecutionContextExecutor): IOAsync[Int] =
    IOAsync {
      val fa = addOne(x)(ec)
      val fb = addOne(y)(ec)

      val result = fa.flatMap(a => fb.map(b => a * b))
      println(s"result $result")

      result.unsafeRun()
    }.evalOn(ec)

  def addOne(x: Int)(ec: ExecutionContextExecutor): IOAsync[Int] =
    IOAsync {
      println(s"addOne $x")
      x + 1
    }.evalOn(ec)

}
