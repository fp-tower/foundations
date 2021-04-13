package answers.action.async.booking

import java.time.Duration

case class Partner(
  name: String,
  commission: Double,
  timeout: Duration,
  client: SearchFlightClient
)
