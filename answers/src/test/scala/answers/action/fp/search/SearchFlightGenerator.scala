package answers.action.fp.search

import java.time._

import answers.action.fp.IO
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen

object SearchFlightGenerator {

  val airportGen: Gen[Airport] =
    Gen.oneOf(
      Airport.londonHeathrow,
      Airport.londonGatwick,
      Airport.melbourne,
      Airport.parisOrly,
      Airport.parisCharlesDeGaulle,
      Airport.tokyoAneda
    )

  val flightGen: Gen[Flight] =
    for {
      flightId <- arbitrary[Short].map(_.toString)
      airline  <- Gen.oneOf("British Airways", "Lufthansa", "Air France", "Ryanair")
      from     <- airportGen
      to       <- airportGen // .filterNot(_ == from)
      duration <- Gen.choose(20, 2400).map(Duration.ofMinutes(_))
      departureAt <- Gen
        .choose(
          LocalDate.of(2020, 1, 1).atStartOfDay(ZoneId.of("UTC")).toEpochSecond,
          LocalDate.of(2060, 1, 1).atStartOfDay(ZoneId.of("UTC")).toEpochSecond
        )
        .map(Instant.ofEpochSecond)
      numberOfStops <- Gen.choose(0, 4)
      cost          <- Gen.choose(0.0, 40000.0)
      redirectLink  <- arbitrary[String]
    } yield Flight(
      flightId = flightId,
      airline = airline,
      from = from,
      to = to,
      departureAt = departureAt,
      duration = duration,
      numberOfStops = numberOfStops,
      unitPrice = cost,
      redirectLink = redirectLink
    )

  val validClientGen: Gen[SearchFlightClient] =
    Gen
      .listOf(flightGen)
      .map { flights =>
        new SearchFlightClient {
          def search(from: Airport, to: Airport, date: LocalDate): IO[List[Flight]] =
            IO(
              flights.map(flight =>
                flight.copy(
                  from = from,
                  to = to,
                  departureAt = date.atTime(flight.departureAt.atOffset(ZoneOffset.UTC).toOffsetTime).toInstant
                )
              )
            )
        }
      }

  val invalidClientGen: Gen[SearchFlightClient] =
    Gen.listOf(flightGen).map(flights => SearchFlightClient.constant(IO(flights)))

  val failingClientGen: Gen[SearchFlightClient] =
    arbitrary[Exception].map(e => SearchFlightClient.constant(IO.fail(e)))

  val clientGen: Gen[SearchFlightClient] =
    Gen.frequency(
      8 -> validClientGen,
      1 -> invalidClientGen,
      1 -> failingClientGen
    )

}
