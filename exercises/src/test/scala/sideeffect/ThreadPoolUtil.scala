package sideeffect

import java.util.concurrent._
import java.util.concurrent.atomic.AtomicInteger

import scala.concurrent.ExecutionContext

// copied and adapted from cats-effect IOApp
object ThreadPoolUtil {

  def fixedSize(threads: Int, prefix: String): ExecutorService =
    Executors.newFixedThreadPool(
      threads,
      new ThreadFactory {
        val ctr = new AtomicInteger(0)
        def newThread(r: Runnable): Thread = {
          val back = new Thread(r)
          back.setName(prefix + "-" + ctr.getAndIncrement())
          back.setDaemon(true)
          back
        }
      }
    )

  def cached(prefix: String): ExecutorService =
    Executors.newCachedThreadPool(new ThreadFactory {
      def newThread(r: Runnable) = {
        val t = new Thread(r, prefix)
        t.setDaemon(true)
        t
      }
    })

  class CounterExecutionContext(underlying: ExecutionContext) extends ExecutionContext {
    val executeCalled: AtomicInteger = new AtomicInteger(0)
    def execute(runnable: Runnable): Unit = {
      executeCalled.incrementAndGet()
      underlying.execute(runnable)
    }
    def reportFailure(cause: Throwable): Unit = underlying.reportFailure(cause)
  }

}
