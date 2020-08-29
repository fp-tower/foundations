package exercises.dataprocessing

import kantan.csv.RowDecoder

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

object Sample {
  implicit val decoder: RowDecoder[Sample] = {
    RowDecoder
      .decoder(0, 1, 2, 3, 4, 5, 6, 7)(Sample.apply)
      // -99 is a no-data flag when data are not available, see https://academic.udayton.edu/kissock/http/Weather/source.htm
      .filter(_.temperature != -99)
  }
}
