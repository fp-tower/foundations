package answers.dataprocessing

import scala.concurrent.duration.Duration
import scala.concurrent.duration._

object TimeUtil {

  def timeOne[A](name: String, block: => A): A = {
    print(s"[ $name ]")
    val (result, d) = _time(block)
    val duration    = Duration.fromNanos(d)
    println(s" Elapsed time: ${duration.pretty}")
    result
  }

  def time[A](numberOfIterations: Int, name: String)(block: => A): Unit = {
    print(s"[ $name ] ")
    val result = Elapsed.fromTime(1.to(numberOfIterations).map(_ => _time(block)._2))
    println(result.toString)
  }

  def bench[A](name: String, numberOfIterations: Int)(sequential: => A, parallel: => A): Unit = {
    println(s"[ $name ]")
    val seq   = Elapsed.fromTime(1.to(numberOfIterations).map(_ => _time(sequential)._2))
    val par   = Elapsed.fromTime(1.to(numberOfIterations).map(_ => _time(parallel)._2))
    val ratio = seq.median.toNanos / par.median.toNanos.toDouble
    println(s"  sequential: $seq")
    println(s"  parallel  : $par")
    println(f"  parallel is ${ratio}%2.2f faster than sequential (median)")
  }

  case class Elapsed(median: FiniteDuration, average: FiniteDuration, min: FiniteDuration, max: FiniteDuration) {
    override def toString: String =
      s"Elapsed median: ${median.pretty} avg: ${average.pretty}, min: ${min.pretty}, max: ${max.pretty}"
  }

  object Elapsed {
    def fromTime(nanos: Seq[Long]): Elapsed = {
      val min    = Duration.fromNanos(nanos.min)
      val max    = Duration.fromNanos(nanos.max)
      val avg    = Duration.fromNanos(nanos.sum / nanos.size)
      val median = Duration.fromNanos(nanos.sorted.apply(nanos.size / 2))
      Elapsed(median, avg, min, max)
    }
  }

  def _time[A](block: => A): (A, Long) = {
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
