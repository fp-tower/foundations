package exercises.dataprocessing

import exercises.dataprocessing.ThreadPoolUtil.fixedSizeExecutionContext
import exercises.dataprocessing.TimeUtil.{bench, Labelled}
import kantan.csv._
import kantan.csv.ops._

// Run the notebook using green arrow (if available in your IDE)
// or run `sbt` in your terminal to open sbt in shell mode then type:
// exercises/runMain exercises.dataprocessing.TemperatureNotebook
object TemperatureNotebook extends App {

  // !!!!  IMPORTANT !!!!
  // Download the dataset from https://www.dropbox.com/s/4pf6h2oxw4u7xsq/city_temperature.csv?dl=0
  // and place the csv file in the resource directory (exercises/src/main/resources)

  // We use kantan.csv library to parse the each csv raw into a case class `Sample`
  // such as 1 column in the csv maps to one field in the case class.
  // See https://nrinaudo.github.io/kantan.csv/rows_as_case_classes.html
  val reader: CsvReader[Either[ReadError, Sample]] = getClass
    .getResource("/city_temperature.csv")
    .asCsvReader[Sample](rfc.withHeader)

  val rows: List[Either[ReadError, Sample]] = reader.toList

  val failures: List[ReadError] = rows.collect { case Left(error)   => error }
  val samples: List[Sample]     = rows.collect { case Right(sample) => sample }

  // we can also extract failures and successes in one go using `partitionMap`
  // val (failures, successes) = rows.partitionMap(identity)

  println(s"Parsed ${samples.size} rows successfully and ${failures.size} rows failed ")

  // a. Implement `samples`, a `ParList` containing all the `Samples` in `successes`.
  // Partition `parSamples` so that it contains 10 partitions of roughly equal size.
  // Note: Check `ParList` companion object
  lazy val parSamples: ParList[Sample] =
    ???

  // b. Implement `minSampleByTemperature` in TemperatureExercises
  lazy val minSampleByTemperature: Option[Sample] =
    TemperatureExercises.minSampleByTemperature(parSamples)

  // c. Implement `averageTemperature` in TemperatureExercises
  lazy val averageTemperature: Option[Double] =
    TemperatureExercises.averageTemperature(parSamples)

  //////////////////////
  // Benchmark ParList
  //////////////////////

  sys.exit(1) // !!! TO REMOVE WHEN YOU START THIS SECTION !!!

  // j. Compare the runtime performance of various implementations of `sum`:
  // * List foldLeft
  // * List map + sum
  // * ParList foldMap
  // * ParList parFoldMap
  bench("sum")(
    Labelled("List foldLeft", () => samples.foldLeft(0.0)((state, sample) => state + sample.temperatureFahrenheit)),
    Labelled("List map + sum", () => samples.map(_.temperatureFahrenheit).sum),
  )

  // k. Compare the runtime performance of various implementations of `min`
  bench("min")(
    Labelled("List minByOption", () => samples.minByOption(_.temperatureFahrenheit))
  )

  //////////////////////////////////////////////
  // Bonus question (not covered by the video)
  //////////////////////////////////////////////

  // Ideas to improve the performance:
  // * generalise min/max to any type with Ordering
  // * n smallest/largest value (e.g. min/max when n = 1)
  // * for min/max, use reduceMap instead of foldMap
  // * for Monoid with a commutative combine functions, we don't have to process intermediate results in order
  // * use Array instead of List to get data locality

}
