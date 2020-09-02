package answers.dataprocessing

import answers.dataprocessing.TimeUtil._
import kantan.csv._
import kantan.csv.ops._

object TemperatureNotebookOld extends App {

  val sampleSize = Int.MaxValue
//  val sampleSize    = 1000000

  val rawData: java.net.URL = getClass.getResource("/city_temperature.csv")

  val reader: CsvReader[Either[ReadError, Sample]] = rawData.asCsvReader[Sample](rfc.withHeader)

  val (failures, successes) = timeOne("load data", reader.take(sampleSize).toList.partitionMap(identity))

  println(s"${failures.size} failed and ${successes.size} succeeded")

  val partitions    = 10
  val partitionSize = successes.length / partitions + 1
  val computeEC     = ThreadPoolUtil.fixedSize(8, "compute")
  val ec            = computeEC
//  val ec = ExecutionContext.global

  val sequentialSamples      = ParList.byPartitionSize(partitionSize, successes)
  val sequentialTemperatures = sequentialSamples.map(_.temperatureFahrenheit)

  val parallelSamples      = sequentialSamples.setExecutionContext(Some(ec))
  val parallelTemperatures = parallelSamples.map(_.temperatureFahrenheit)

  println(s"Min date is ${parallelSamples.minBy(_.localDate)}")
  println(s"Max date is ${parallelSamples.maxBy(_.localDate)}")

  println(s"Min temperature is ${parallelTemperatures.min}")
  println(s"Max temperature is ${parallelTemperatures.max}")

  val sumTemperature = parallelTemperatures.sum
  val size           = parallelTemperatures.size

  val avgTemperature = sumTemperature / size

  println(s"Average temperature is $avgTemperature")

  val summary = parallelTemperatures.foldMap(SummaryV1.one)(SummaryV1.monoid)

  println(s"Temperature summary is $summary")

  time(100, "sum sequential") { sequentialTemperatures.sum }
  time(100, "max sequential") { sequentialTemperatures.max }
  time(100, "min sequential") { sequentialTemperatures.min }
  time(100, "summary global sequential") {
    sequentialTemperatures.reduceMap(Summary.one)(Summary.semigroup)
  }
  time(100, "summary perCity sequential") {
    sequentialSamples.reduceMap(perCity)(Monoid.map(Summary.semigroup))
  }
  time(100, "summary allLocations sequential") {
    sequentialSamples.reduceMap(allLocations)(Monoid.map(Summary.semigroup))
  }

  time(100, "sum parallel") { parallelTemperatures.sum }
  time(100, "max parallel") { parallelTemperatures.max }
  time(100, "min parallel") { parallelTemperatures.min }
  time(100, "summaryV1 global parallel") {
    parallelTemperatures.foldMap(SummaryV1.one)(SummaryV1.monoid)
  }
  time(100, "summary global parallel") {
    parallelTemperatures.reduceMap(Summary.one)(Summary.semigroup)
  }
  time(100, "summary perCity summary parallel") {
    parallelSamples.reduceMap(perCity)(Monoid.map(Summary.semigroup))
  }
  time(100, "summary allLocations summary parallel") {
    parallelSamples.reduceMap(allLocations)(Monoid.map(Summary.semigroup))
  }

  def perCity(sample: Sample): Map[String, Summary] =
    Map(
      sample.city -> Summary.one(sample.temperatureFahrenheit)
    )

  def allLocations(sample: Sample): Map[String, Summary] = {
    val summary = Summary.one(sample.temperatureFahrenheit)
    Map(
      sample.region              -> summary,
      sample.country             -> summary,
      sample.state.getOrElse("") -> summary,
      sample.city                -> summary,
    )
  }
}
