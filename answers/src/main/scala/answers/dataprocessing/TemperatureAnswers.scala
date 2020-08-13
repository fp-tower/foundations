package answers.dataprocessing

import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._
import TimeUtil._

object TemperatureAnswers extends App {

//  val sampleSize = Int.MaxValue
  val sampleSize = 1000

  val rawData: java.net.URL = getClass.getResource("/city_temperature.csv")

  case class Sample(
    region: String,
    country: String,
    state: Option[String],
    city: String,
    month: Int,
    day: Int,
    year: Int,
    temperature: Double
  )

  val reader: CsvReader[Either[ReadError, Sample]] = rawData.asCsvReader[Sample](rfc.withHeader)

  val (failures, successes) = time(reader.take(sampleSize).toList.partitionMap(identity))

  println(s"${failures.size} failed and ${successes.size} succeeded")

}
