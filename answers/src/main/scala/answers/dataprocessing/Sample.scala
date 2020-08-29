package answers.dataprocessing

import java.time.LocalDate

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
) {
  def localDate: LocalDate =
    LocalDate.of(year, month, day)
}

object Sample {
  implicit val decoder: RowDecoder[Sample] = {
    RowDecoder
      .decoder(0, 1, 2, 3, 4, 5, 6, 7)(Sample.apply)
      .filter(_.temperature != -99)
  }
}
