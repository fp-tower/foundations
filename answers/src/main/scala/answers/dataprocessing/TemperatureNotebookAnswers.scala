package answers.dataprocessing

import kantan.csv._
import kantan.csv.ops._

object TemperatureNotebookAnswers extends App {

  val reader: CsvReader[Either[ReadError, Sample]] = getClass
    .getResource("/city_temperature.csv")
    .asCsvReader[Sample](rfc.withHeader)

  val maxRows    = Int.MaxValue
  val partitions = 10

  val rows: List[Either[ReadError, Sample]] = reader.toList

  val (failures, successes) = rows.partitionMap(identity)

  println(s"Parsed ${successes.size} rows successfully and ${failures.size} rows failed ")

  val computeEC = ThreadPoolUtil.fixedSize(8, "compute")

  val samples: ParList[Sample] =
    ParList.byNumberOfPartition(computeEC, partitions, successes)

  samples.partitions.zipWithIndex.foreach {
    case (p, i) => println(s"Partition $i has size: ${p.size}")
  }

  val minSampleByTemperature: Option[Sample] =
    TemperatureAnswers.minSampleByTemperature(samples)

  println(s"Min sample by temperature is $minSampleByTemperature")
  println(s"Max sample by temperature is ${samples.maxBy(_.temperatureFahrenheit)}")
  println(s"Min sample by date is ${samples.minBy(_.localDate)}")
  println(s"Max sample by date is ${samples.maxBy(_.localDate)}")

  val averageTemperature: Option[Double] =
    TemperatureAnswers.averageTemperature(samples)

  println(s"Average temperature is $averageTemperature")

}
