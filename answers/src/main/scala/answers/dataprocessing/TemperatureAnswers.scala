package answers.dataprocessing

import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._
import TimeUtil._

import scala.concurrent.ExecutionContext

object TemperatureAnswers extends App {

  val sampleSize = Int.MaxValue
//  val sampleSize    = 1000000

  val rawData: java.net.URL = getClass.getResource("/city_temperature.csv")

  case class Sample(
    region: String,
    country: String,
    state: Option[String],
    city: String,
    month: Int,
    day: Int,
    year: Int,
    temperature: Double
  )

  val reader: CsvReader[Either[ReadError, Sample]] = rawData.asCsvReader[Sample](rfc.withHeader)

  val (failures, successes) = timeOne(reader.take(sampleSize).toList.partitionMap(identity))

  println(s"${failures.size} failed and ${successes.size} succeeded")

  val partitions    = 10
  val partitionSize = successes.length / partitions + 1
  val computeEC     = ThreadPoolUtil.fixedSize(partitions, "compute")
//  val ec            = Some(computeEC)
//  val ec = Some(ExecutionContext.global)
  val ec = None

  val samples = ParList
    .partition(partitionSize, successes)
    .setExecutionContext(ec)

  val temperatures = samples.map(_.temperature)

  time(100) { temperatures.sequential.foldMap(identity)(Monoid.sumDouble) }
  time(100) { temperatures.parallel(computeEC).foldMap(identity)(Monoid.sumDouble) }

  time(100) { temperatures.sequential.foldMap(Option(_))(Monoid.maxOption) }
  time(100) { temperatures.parallel(computeEC).foldMap(Option(_))(Monoid.maxOption) }

  time(100) { temperatures.sequential.reducedMap(identity)(Semigroup.max) }
  time(100) { temperatures.parallel(computeEC).reducedMap(identity)(Semigroup.max) }

  time(100) {
    val maxTemperature = ParList.max(temperatures)
    println(s"Max temperature is $maxTemperature")

    val minTemperature = ParList.min(temperatures)
    println(s"Min temperature is $minTemperature")

    val sumTemperature = ParList.sum(temperatures)
    val size           = temperatures.size

    val avgTemperature = sumTemperature / size

    println(s"Average temperature is $avgTemperature")
  }

  time(100) {
    temperatures.foldMap(Summary.one)(Summary.monoid)
  }

}
