package exercises.dataprocessing

import kantan.csv._
import kantan.csv.ops._

object TemperatureNotebook extends App {

  // !!!!  IMPORTANT !!!!
  // Download the dataset from https://www.kaggle.com/sudalairajkumar/daily-temperature-of-major-cities
  // and place the csv file in the resource directory (exercises/src/main/resources)

  // We use kantan.csv library to parse the each csv raw into a case class `Sample`
  // such as 1 column in the csv maps to one field in the case class.
  // See https://nrinaudo.github.io/kantan.csv/rows_as_case_classes.html
  val reader: CsvReader[Either[ReadError, Sample]] = getClass
    .getResource("/city_temperature.csv")
    .asCsvReader[Sample](rfc.withHeader)

  // Maximum number of rows to load.
  // Use a small value to speed-up experiment.
  // Use Int.MaxValue to load the entire csv file.
  val maxRows = 10000 // Int.MaxValue

  val (failures, successes) = reader.take(maxRows).toList.partitionMap(identity)

  println(s"${failures.size} rows failed and ${successes.size} rows succeeded")

  // a. Implement `samples`, a `ParList` containing all the parsed rows.
  // Partition `samples` so that it contains 10 partitions of roughly equal size.
  // Note: Check `ParList` companion object
  lazy val samples: ParList[Sample] =
    ???

  // b. Implement `minTemperature` in TemperatureExercises
  lazy val minSample: Option[Sample] =
    TemperatureExercises.minSample(samples)

  // c. Implement `averageTemperature` in TemperatureExercises
  lazy val averageTemperature: Option[Double] =
    TemperatureExercises.averageTemperature(samples)

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
