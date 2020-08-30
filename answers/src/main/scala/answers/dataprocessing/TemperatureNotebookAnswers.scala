package answers.dataprocessing

import kantan.csv._
import kantan.csv.ops._

object TemperatureNotebookAnswers extends App {

  val reader: CsvReader[Either[ReadError, Sample]] = getClass
    .getResource("/city_temperature.csv")
    .asCsvReader[Sample](rfc.withHeader)

  val maxRows    = Int.MaxValue
  val partitions = 10

  val (failures, successes) = reader.take(maxRows).toList.partitionMap(identity)

  println(s"${failures.size} rows failed and ${successes.size} rows succeeded")

  val samples: ParList[Sample] =
    ParList.byNumberOfPartition(partitions, successes)

  samples.partitions.zipWithIndex.foreach {
    case (p, i) => println(s"Partition $i has size: ${p.size}")
  }

  val minSampleByTemperature: Option[Sample] =
    TemperatureAnswers.minSampleByTemperature(samples)

  println(s"Min sample is $minSampleByTemperature")

  val averageTemperature: Option[Double] =
    TemperatureAnswers.averageTemperature(samples)

  println(s"Average temperature is $averageTemperature")

}
