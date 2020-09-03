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

  val parSamples = ParList.byPartitionSize(computeEC, partitionSize, samples)

  println(s"Min date is ${parSamples.minBy(_.localDate)}")
  println(s"Max date is ${parSamples.maxBy(_.localDate)}")

  println(s"Min temperature is ${parSamples.minBy(_.temperatureFahrenheit)}")
  println(s"Max temperature is ${parSamples.maxBy(_.temperatureFahrenheit)}")

  val sumTemperature = parSamples.foldMap(_.temperatureFahrenheit)(Monoid.sumNumeric)
  val size           = samples.size

  val avgTemperature = sumTemperature / size

  println(s"Average temperature is $avgTemperature")

  val summary = parSamples.foldMap(s => SummaryV1.one(s.temperatureFahrenheit))(SummaryV1.monoid)

  println(s"Temperature summary is $summary")

  bench("sum", 100)(
    reference = samples.foldLeft(0.0)((state, sample) => state + sample.temperatureFahrenheit),
    newImpl = parSamples.foldMap(_.temperatureFahrenheit)(Monoid.sumNumeric),
  )

  bench("min", 100)(
    reference = samples.minByOption(_.temperatureFahrenheit),
    newImpl = parSamples.minBy(_.temperatureFahrenheit),
  )

  bench("max", 100)(
    reference = samples.maxBy(_.temperatureFahrenheit),
    newImpl = parSamples.maxBy(_.temperatureFahrenheit),
  )

  bench("summary V1 global", 100)(
    reference = parSamples.foldMapSequential(s => SummaryV1.one(s.temperatureFahrenheit))(SummaryV1.monoid),
    newImpl = parSamples.foldMap(s => SummaryV1.one(s.temperatureFahrenheit))(SummaryV1.monoid),
  )

  bench("summary global", 100)(
    reference = parSamples.reducedMapSequential(s => Summary.one(s.temperatureFahrenheit))(Summary.semigroup),
    newImpl = parSamples.reduceMap(s => Summary.one(s.temperatureFahrenheit))(Summary.semigroup),
  )

  bench("summary perCity", 100)(
    reference = parSamples.reducedMapSequential(perCity)(Monoid.map(Summary.semigroup)),
    newImpl = parSamples.reduceMap(perCity)(Monoid.map(Summary.semigroup)),
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
