package exercises.dataprocessing

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.{Executors, ThreadFactory}

import scala.concurrent.ExecutionContext

// copied and adapted from cats-effect IOApp
object ThreadPoolUtil {

  def fixedSizeExecutionContext(threads: Int, prefix: String = "compute"): ExecutionContext =
    ExecutionContext.fromExecutor(
      Executors.newFixedThreadPool(threads, threadFactory(prefix, daemon = true))
    )

  def cachedExecutionContext(prefix: String): ExecutionContext =
    ExecutionContext.fromExecutor(
      Executors.newCachedThreadPool(threadFactory(prefix, daemon = true))
    )

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

}
