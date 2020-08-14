package answers.dataprocessing

import scala.concurrent.duration.Duration
import scala.concurrent.duration._

object TimeUtil {

  def timeOne[R](block: => R): R = {
    println(s"|-")
    val (result, d) = _time(block)
    val duration    = Duration.fromNanos(d)
    println(s"-| Elapsed time: ${duration.pretty}")
    result
  }

  def time[R](numberOfIterations: Int)(block: => R): R = {
    println(s"|-")
    val results   = 1.to(numberOfIterations).map(_ => _time(block))
    val durations = results.map(_._2)
    val min       = Duration.fromNanos(durations.min)
    val max       = Duration.fromNanos(durations.max)
    val avg       = Duration.fromNanos(durations.sum / numberOfIterations)
    val median    = Duration.fromNanos(durations.sorted.apply(numberOfIterations / 2))
    println(s"-| Elapsed median: ${median.pretty} avg: ${avg.pretty}, min: ${min.pretty}, max: ${max.pretty}")
    results.head._1
  }

  def _time[R](block: => R): (R, Long) = {
    val t0       = System.nanoTime()
    val result   = block // call-by-name
    val t1       = System.nanoTime()
    val duration = t1 - t0
    (result, duration)
  }

  // copied from https://alvinalexander.com/java/jwarehouse/
  implicit class PrettyPrintableDuration(val duration: Duration) extends AnyVal {

    def pretty: String = pretty(includeNanos = false)

    /** Selects most apropriate TimeUnit for given duration and formats it accordingly */
    def pretty(includeNanos: Boolean, precision: Int = 4): String = {
      require(precision > 0, "precision must be > 0")

      duration match {
        case d: FiniteDuration =>
          val nanos = d.toNanos
          val unit  = chooseUnit(nanos)
          val value = nanos.toDouble / NANOSECONDS.convert(1, unit)

          s"%.${precision}g %s%s".format(value, abbreviate(unit), if (includeNanos) s" ($nanos ns)" else "")

        case d: Duration.Infinite if d == Duration.MinusInf => s"+∞ (minus infinity)"
        case _                                              => s"-∞ (infinity)"
      }
    }

    def chooseUnit(nanos: Long): TimeUnit = {
      val d = nanos.nanos

      if (d.toDays > 0) DAYS
      else if (d.toHours > 0) HOURS
      else if (d.toMinutes > 0) MINUTES
      else if (d.toSeconds > 0) SECONDS
      else if (d.toMillis > 0) MILLISECONDS
      else if (d.toMicros > 0) MICROSECONDS
      else NANOSECONDS
    }

    def abbreviate(unit: TimeUnit): String = unit match {
      case NANOSECONDS  => "ns"
      case MICROSECONDS => "μs"
      case MILLISECONDS => "ms"
      case SECONDS      => "s"
      case MINUTES      => "min"
      case HOURS        => "h"
      case DAYS         => "d"
    }
  }

}
