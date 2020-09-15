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

  // j. Compare the runtime performance of various implementations of `sum`:
  // * List foldLeft
  // * List map + sum
  // * ParList foldMap
  // * ParList parFoldMap
  bench("sum", ignore = true)(
    Labelled("List foldLeft", () => samples.foldLeft(0.0)((state, sample) => state + sample.temperatureFahrenheit)),
    Labelled("List map + sum", () => samples.map(_.temperatureFahrenheit).sum),
  )

  // k. Implement `summaryListOnePass` and `summaryParList`
  // Compare the runtime performance of various implementations of `summary`
  bench("summary", ignore = true)(
    Labelled("List", () => TemperatureExercises.summaryList(samples)),
    Labelled("List one-pass", () => TemperatureExercises.summaryListOnePass(samples))
  )

  // l. Add `summaryParListOnePass` to the benchmark above.

  //////////////////////////////////////////////
  // Bonus question (not covered by the video)
  //////////////////////////////////////////////

  // Calculate the n smallest/largest value, what are the 5 smallest values in the dataset

  // Generalise sum to take all types of number (Hint: check `Numeric`)

  // Implement a shuffle method or constructor so that it is unlikely all the expensive
  // tasks are in the same partition. Will it change the results of `parFoldMap`?

  //////////////////////////////////////////////
  // Ideas to improve `ParList` performance
  //////////////////////////////////////////////

  // 1. When we defined `Summary`, we made `min` and `max` an `Option` because the `ParList`
  //   can be empty. However, it is quite expensive because we wrap and unwrap an Option for
  //   every value in the dataset. Instead we could check if the `ParList` is empty at the beginning,
  //   if it is we return None, otherwise we can `reduce` the `ParList` without `Option`.
  //   See `reduceFoldLeftOption` on `List`.
  //
  //   Could you implement `reduceMap` on `ParList`?
  //   def reduceMap[To](zoom: A => To)(combine: (To, To) => To): Option[To]
  //   or
  //   def reduceMap[To](zoom: A => To)(semigroup: Semigroup[To]): Option[To]
  //   where `Semigroup` is like a `Monoid` but without `default` value.

  // 2. use Array instead of List as underlying data structure for better caching.
  //   Furthermore, we only need to store a partition as a pair of index:
  //   partition 1 from 0      to 10 000
  //   partition 2 from 10 001 to 25 000

  // 3. `parFoldMap` need to wait for ALL intermediate results to be ready before starting
  //    to fold them. Instead, could we fold the intermediate results as soon as they
  //    are available? Will we always get the same results this way?

}
