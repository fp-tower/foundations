package answers.action.fp.booking

import java.time.{Duration, Instant}

case class Flight(
  flightId: String,
  airline: String,
  from: Airport,
  to: Airport,
  departureAt: Instant,
  duration: Duration,
  numberOfStops: Int,
  cost: Double, // in dollars for one passenger
  redirectLink: String,
)
