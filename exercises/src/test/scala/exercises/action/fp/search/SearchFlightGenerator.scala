package exercises.action.fp.search

import java.time._

import exercises.action.fp.IO
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
      airline  <- Gen.oneOf("British Airways", "Lufthansa", "Air France", "Lastminute.com")
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
      unitPrice     <- Gen.choose(0.0, 40000.0)
      redirectLink  <- arbitrary[String]
    } yield Flight(
      flightId = flightId,
      airline = airline,
      from = from,
      to = to,
      departureAt = departureAt,
      duration = duration,
      numberOfStops = numberOfStops,
      unitPrice = unitPrice,
      redirectLink = redirectLink
    )

  val successfulClientGen: Gen[SearchFlightClient] =
    Gen.listOf(flightGen).map(flights => SearchFlightClient.constant(IO(flights)))

  val failingClientGen: Gen[SearchFlightClient] =
    arbitrary[Throwable].map(e => SearchFlightClient.constant(IO.fail(e)))

  val clientGen: Gen[SearchFlightClient] =
    Gen.frequency(
      9 -> successfulClientGen,
      1 -> failingClientGen
    )

}
