package answers.dataprocessing

import scala.concurrent.duration.Duration
import scala.concurrent.duration._

object TimeUtil {

  def bench[A](operation: String, iterations: Int = 100, warmUpIterations: Int = 10, ignore: Boolean = false)(
    function1: Labelled[() => A],
    otherFunctions: Labelled[() => A]*,
  ): Unit =
    if (ignore) ()
    else {
      println(s"[ $operation ]")
      println(s"  $iterations iterations, $warmUpIterations warm-up iterations")
      val totalIterations = iterations + warmUpIterations
      val allFunctions    = function1 :: otherFunctions.toList
      val maxLabelLength  = allFunctions.map(_.name.length).max
      val times = allFunctions
        .map(
          _.map(
            function => 1.to(totalIterations).map(_ => time(function())._2).drop(warmUpIterations)
          ).map(Elapsed.fromTime)
        )
        .sortBy(_.value.median)

      times.foreach(label => println(s"  ${label.padName(maxLabelLength)}: ${label.value}"))
    }

  case class Labelled[+A](name: String, value: A) {
    def map[To](update: A => To): Labelled[To] =
      copy(value = update(value))

    def padName(minLength: Int): String =
      name + List.fill(minLength - name.length)(" ").mkString
  }

  case class Elapsed(median: FiniteDuration, average: FiniteDuration, min: FiniteDuration, max: FiniteDuration) {
    override def toString: String =
      s" median: ${median.pretty}, avg: ${average.pretty}, min: ${min.pretty}, max: ${max.pretty}"
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

  def timeOne[A](name: String)(block: => A): A = {
    print(s"[ $name ]")
    val (result, d) = time(block)
    val duration    = Duration.fromNanos(d)
    println(s" Elapsed time: ${duration.pretty}")
    result
  }

  def time[A](block: => A): (A, Long) = {
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
