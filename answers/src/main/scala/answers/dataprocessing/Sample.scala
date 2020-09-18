package answers.dataprocessing

import java.time.LocalDate

import kantan.csv.RowDecoder

case class Sample(
  region: String, // e.g. Africa, Asia, Australia/South Pacific, Europe, Middle East, North America
  country: String, // e.g. Algeria, Burundi, Benin, Central African Republic, Congo
  state: Option[String], // U.S. specific e.g. Alabama, Alaska, Arizona
  city: String, // e.g. Algiers, Bujumbura, Cotonou, Bangui, Brazzaville
  month: Int,
  day: Int,
  year: Int,
  temperatureFahrenheit: Double
) {
  val temperatureCelsius: Double =
    (temperatureFahrenheit - 32) * 5 / 9

  def localDate: LocalDate =
    LocalDate.of(year, month, day)

  override def toString: String =
    s"Sample(temperature = ${formatTemperature(temperatureFahrenheit)} F° / ${formatTemperature(temperatureCelsius)} C°, " +
      s"region = $region, " +
      s"country = $country, " +
      s"state = ${state.getOrElse("N/A")}, " +
      s"city = $city, date = ${year}-${month}-${day})"

  def formatTemperature(temperature: Double): String =
    f"$temperature%.2f"

}

object Sample {
  implicit val decoder: RowDecoder[Sample] = {
    RowDecoder
      .decoder(0, 1, 2, 3, 4, 5, 6, 7)(Sample.apply)
      .filter(_.temperatureFahrenheit != -99)
  }
}
