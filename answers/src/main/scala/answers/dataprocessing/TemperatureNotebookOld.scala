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

  val parSamples      = ParList.byNumberOfPartition(computeEC, 10, samples)
  val samplesArray    = samples.toArray
  val parSamplesArray = ParArray(computeEC, samplesArray, partitionSize)

  println(s"Min date is ${parSamples.minBy(_.localDate)}")
  println(s"Max date is ${parSamples.maxBy(_.localDate)}")

  println(s"Min temperature is ${parSamples.minBy(_.temperatureFahrenheit)}")
  println(s"Max temperature is ${parSamples.maxBy(_.temperatureFahrenheit)}")

  val sumTemperature = parSamples.foldMap(_.temperatureFahrenheit)(Monoid.sumNumeric)
  val size           = samples.size

  val avgTemperature = sumTemperature / size

  println(s"Average temperature is $avgTemperature")

  println(s"Temperature summary is ${parSamples.foldMap(summaryV1)(SummaryV1.monoid)}")

  bench("sum")(
    Labelled("ParList foldMap", () => parSamples.foldMap(_.temperatureFahrenheit)(Monoid.sumNumeric)),
    Labelled("ParList foldMapSequential",
             () => parSamples.foldMapSequential(_.temperatureFahrenheit)(Monoid.sumNumeric)),
    Labelled("List foldLeft", () => samples.foldLeft(0.0)((state, sample) => state + sample.temperatureFahrenheit)),
    Labelled("ParArray foldMap", () => parSamplesArray.foldMap(_.temperatureFahrenheit)(Monoid.sumNumeric)),
    Labelled("Array foldLeft",
             () => samplesArray.foldLeft(0.0)((state, sample) => state + sample.temperatureFahrenheit)),
  )

  bench("min")(
    Labelled("ParList minBy", () => parSamples.minBy(_.temperatureFahrenheit)),
    Labelled("List minByOption", () => samples.minByOption(_.temperatureFahrenheit)),
  )

  bench("summary global")(
    Labelled("ParList foldMap", () => parSamples.foldMap(summaryV1)(SummaryV1.monoid)),
    Labelled("ParList foldMapSequential", () => parSamples.foldMapSequential(summaryV1)(SummaryV1.monoid)),
    Labelled(
      "ParList reduceMap",
      () => SummaryV1.fromSummary(parSamples.reduceMap(summary)(Summary.semigroup))
    ),
    Labelled("ParList reducedMapSequential",
             () =>
               SummaryV1.fromSummary(
                 parSamples.reducedMapSequential(summary)(Summary.semigroup)
             )),
    Labelled(
      "ParArray reduceMap",
      () => SummaryV1.fromSummary(parSamplesArray.reduceMap(summary)(Summary.semigroup))
    ),
  )

  bench("summary perCity")(
    Labelled("ParList reduceMap", () => parSamples.reduceMap(perCity)(Monoid.map(Summary.semigroup))),
    Labelled("ParList reducedMapSequential",
             () => parSamples.reducedMapSequential(perCity)(Monoid.map(Summary.semigroup))),
  )

  def summaryV1(sample: Sample): SummaryV1 =
    SummaryV1.one(sample.temperatureFahrenheit)

  def summary(sample: Sample): Summary =
    Summary.one(sample.temperatureFahrenheit)

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
