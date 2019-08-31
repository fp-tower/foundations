package exercises.sideeffect

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.{ExecutorService, Executors, ThreadFactory}

import scala.concurrent.ExecutionContext

// copied and adapted from cats-effect IOApp
object ThreadPoolUtil {

  def fixedSize(threads: Int, prefix: String): ExecutorService =
    Executors.newFixedThreadPool(threads, threadFactory(prefix, daemon = true))

  def cached(prefix: String): ExecutorService =
    Executors.newCachedThreadPool(threadFactory(prefix, daemon = true))

  def threadFactory(prefix: String, daemon: Boolean): ThreadFactory =
    new ThreadFactory {
      val ctr = new AtomicInteger(0)
      def newThread(r: Runnable): Thread = {
        val back = new Thread(r)
        back.setName(prefix + "-" + ctr.getAndIncrement())
        back.setDaemon(daemon)
        back
      }
    }

  class CounterExecutionContext(underlying: ExecutionContext) extends ExecutionContext {
    val executeCalled: AtomicInteger = new AtomicInteger(0)
    def execute(runnable: Runnable): Unit = {
      executeCalled.incrementAndGet()
      underlying.execute(runnable)
    }
    def reportFailure(cause: Throwable): Unit = underlying.reportFailure(cause)
  }

}
