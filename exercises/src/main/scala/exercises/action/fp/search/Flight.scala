package exercises.action.fp.search

import java.time.{Duration, Instant}

case class Flight(
  flightId: String,
  airline: String,
  from: Airport,
  to: Airport,
  departureAt: Instant,
  duration: Duration,
  numberOfStops: Int, // direct = 0
  unitPrice: Double, // in dollars
  redirectLink: String,
)
