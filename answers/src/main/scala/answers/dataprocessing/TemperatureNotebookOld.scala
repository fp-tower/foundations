package answers.dataprocessing

import answers.dataprocessing.TimeUtil._
import kantan.csv._
import kantan.csv.ops._

object TemperatureNotebookOld extends App {

  val rawData: java.net.URL = getClass.getResource("/city_temperature.csv")

  val reader: CsvReader[Either[ReadError, Sample]] = rawData.asCsvReader[Sample](rfc.withHeader)

  val (failures, samples) = timeOne("load data", reader.toList.partitionMap(identity))

  println(s"${failures.size} failed and ${samples.size} succeeded")

  val partitions    = 10
  val partitionSize = samples.length / partitions + 1
  val computeEC     = ThreadPoolUtil.fixedSize(8, "compute")

  val parSamples   = ParList.byPartitionSize(computeEC, partitionSize, samples)
  val temperatures = samples.map(_.temperatureFahrenheit)

  println(s"Min date is ${samples.minBy(_.localDate)}")
  println(s"Max date is ${samples.maxBy(_.localDate)}")

  println(s"Min temperature is ${temperatures.min}")
  println(s"Max temperature is ${temperatures.max}")

  val sumTemperature = temperatures.sum
  val size           = samples.size

  val avgTemperature = sumTemperature / size

  println(s"Average temperature is $avgTemperature")

  val summary = parSamples.foldMap(s => SummaryV1.one(s.temperatureFahrenheit))(SummaryV1.monoid)

  println(s"Temperature summary is $summary")

  bench("sum", 100)(
    sequential = samples.foldLeft(0.0)((state, sample) => state + sample.temperatureFahrenheit),
    parallel = parSamples.foldMap(_.temperatureFahrenheit)(Monoid.sumNumeric),
  )

  bench("min", 100)(
    sequential = samples.minByOption(_.temperatureFahrenheit),
    parallel = parSamples.minBy(_.temperatureFahrenheit),
  )

  bench("max", 100)(
    sequential = samples.maxBy(_.temperatureFahrenheit),
    parallel = parSamples.maxBy(_.temperatureFahrenheit),
  )

  bench("summary V1 global", 100)(
    sequential = parSamples.foldMapSequential(s => SummaryV1.one(s.temperatureFahrenheit))(SummaryV1.monoid),
    parallel = parSamples.foldMap(s => SummaryV1.one(s.temperatureFahrenheit))(SummaryV1.monoid),
  )

  bench("summary global", 100)(
    sequential = parSamples.reducedMapSequential(s => Summary.one(s.temperatureFahrenheit))(Summary.semigroup),
    parallel = parSamples.reduceMap(s => Summary.one(s.temperatureFahrenheit))(Summary.semigroup),
  )

  bench("summary global", 100)(
    sequential = parSamples.reducedMapSequential(perCity)(Monoid.map(Summary.semigroup)),
    parallel = parSamples.reduceMap(perCity)(Monoid.map(Summary.semigroup)),
  )

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
