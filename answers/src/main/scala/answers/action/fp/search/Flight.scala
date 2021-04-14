package answers.action.fp.search

import java.time.{Duration, Instant, LocalDate}

case class Flight(
  flightId: String,
  airline: String,
  from: Airport,
  to: Airport,
  departureAt: Instant,
  duration: Duration,
  numberOfStops: Int,
  unitPrice: Double, // in dollars for one passenger
  redirectLink: String,
) {
  def departureDate: LocalDate =
    departureAt.atZone(from.timeZone).toLocalDate
}
