package answers.dataprocessing

import kantan.csv._
import kantan.csv.ops._

object TemperatureNotebookAnswers extends App {

  val reader: CsvReader[Either[ReadError, Sample]] = getClass
    .getResource("/city_temperature.csv")
    .asCsvReader[Sample](rfc.withHeader)

  val maxRows = 10000 // Int.MaxValue

  val (failures, successes) = reader.take(maxRows).toList.partitionMap(identity)

  println(s"${failures.size} rows failed and ${successes.size} rows succeeded")

  val partitions    = 10
  val partitionSize = successes.length / partitions + 1

  val samples: ParList[Sample] =
    ParList.partition(partitionSize, successes)

  val minSample: Option[Sample] =
    TemperatureAnswers.minSample(samples)

  println(s"Min sample is $minSample")

  val averageTemperature: Option[Double] =
    TemperatureAnswers.averageTemperature(samples)

  println(s"Average temperature is $averageTemperature")

}
