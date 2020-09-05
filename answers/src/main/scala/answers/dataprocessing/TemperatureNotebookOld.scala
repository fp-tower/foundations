package answers.dataprocessing

import answers.dataprocessing.TimeUtil._
import kantan.csv._
import kantan.csv.ops._

object TemperatureNotebookOld extends App {

  val rawData: java.net.URL = getClass.getResource("/city_temperature.csv")

  val reader: CsvReader[Either[ReadError, Sample]] = rawData.asCsvReader[Sample](rfc.withHeader)

  val (failures, samples) = timeOne("load data")(reader.toList.partitionMap(identity))

  println(s"Parsed ${samples.size} rows successfully and ${failures.size} rows failed")

  val partitions    = 10
  val partitionSize = samples.length / partitions + 1
  val computeEC     = ThreadPoolUtil.fixedSizeExecutionContext(8, "compute")

  val parSamples      = ParList.byNumberOfPartition(computeEC, 10, samples)
  val samplesArray    = samples.toArray
  val parSamplesArray = ParArray(computeEC, samplesArray, partitionSize)

  println(s"Min date is ${parSamples.minBy(_.localDate)}")
  println(s"Max date is ${parSamples.maxBy(_.localDate)}")

  println(s"Min temperature is ${parSamples.minBy(_.temperatureFahrenheit)}")
  println(s"Max temperature is ${parSamples.maxBy(_.temperatureFahrenheit)}")

  val sumTemperature = parSamples.parFoldMap(_.temperatureFahrenheit)(Monoid.sumNumeric)
  val size           = samples.size

  val avgTemperature = sumTemperature / size

  println(s"Average temperature is $avgTemperature")

  println(s"Temperature summary is ${parSamples.parFoldMap(SummaryV1.one)(SummaryV1.monoid)}")

  bench("sum Samples")(
    Labelled("List foldLeft", () => samples.foldLeft(0.0)((state, sample) => state + sample.temperatureFahrenheit)),
    Labelled("ParList foldMap", () => parSamples.foldMap(_.temperatureFahrenheit)(Monoid.sumNumeric)),
    Labelled("ParList parFoldMap", () => parSamples.parFoldMap(_.temperatureFahrenheit)(Monoid.sumNumeric)),
    Labelled("ParArray parFoldMap", () => parSamplesArray.parFoldMap(_.temperatureFahrenheit)(Monoid.sumNumeric)),
    Labelled("Array foldLeft",
             () => samplesArray.foldLeft(0.0)((state, sample) => state + sample.temperatureFahrenheit)),
  )

  bench("min")(
    Labelled("ParList minBy", () => parSamples.minBy(_.temperatureFahrenheit)),
    Labelled("List minByOption", () => samples.minByOption(_.temperatureFahrenheit)),
  )

  bench("summary")(
    Labelled("List", () => TemperatureAnswers.summaryList(samples)),
    Labelled("List one-pass", () => TemperatureAnswers.summaryListOnePass(samples)),
    Labelled("ParList one-pass foldMap hard-coded Monoid",
             () => parSamples.parFoldMap(SummaryV1.one)(SummaryV1.monoid)),
    Labelled("ParList one-pass foldMap derived Monoid",
             () => parSamples.parFoldMap(SummaryV1.one)(SummaryV1.monoidDerived)),
    Labelled("ParList one-pass reduceMap hard-coded Semigroup",
             () => SummaryV1.fromSummary(parSamples.parReduceMap(Summary.one)(Summary.semigroup))),
    Labelled("ParList one-pass reduceMap derived Semigroup",
             () => SummaryV1.fromSummary(parSamples.parReduceMap(Summary.one)(Summary.semigroupDerived))),
  )

  bench("summary perCity")(
    Labelled("ParList reducedMap", () => parSamples.reducedMap(perCity)(Monoid.map(Summary.semigroup))),
    Labelled("ParList parReduceMap", () => parSamples.parReduceMap(perCity)(Monoid.map(Summary.semigroup))),
  )

  def perCity(sample: Sample): Map[String, Summary] =
    Map(
      sample.city -> Summary.one(sample)
    )

  def allLocations(sample: Sample): Map[String, Summary] = {
    val summary = Summary.one(sample)
    Map(
      sample.region              -> summary,
      sample.country             -> summary,
      sample.state.getOrElse("") -> summary,
      sample.city                -> summary,
    )
  }
}
