package answers.action.fp.booking

import java.time.{Duration, Instant, LocalDate, LocalTime, ZoneId, ZoneOffset}

import answers.action.fp.IO
import answers.action.fp.booking.FlightPredicate.{CheaperThan, Direct, OneStop, ShorterThan, TwoOrMoreStops}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen

object Generator {

  val basicPredicateGen: Gen[FlightPredicate] =
    Gen.oneOf(
      Gen.const(Direct),
      Gen.const(OneStop),
      Gen.const(TwoOrMoreStops),
      Gen.choose(1, 30).map(Duration.ofHours(_)).map(ShorterThan(_)),
      Gen.choose(0, 40000).map(CheaperThan(_))
    )

  val predicateGen: Gen[FlightPredicate] =
    Gen.lzy(
      Gen.frequency(
        4 -> basicPredicateGen,
        1 -> Gen.zip(predicateGen, predicateGen).map { case (lhs, rhs) => lhs && rhs },
        1 -> Gen.zip(predicateGen, predicateGen).map { case (lhs, rhs) => lhs || rhs },
      )
    )

  val airportGen: Gen[Airport] =
    Gen.oneOf(Airport.all)

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
    } yield
      Flight(
        flightId = flightId,
        airline = airline,
        from = from,
        to = to,
        departureAt = departureAt,
        duration = duration,
        numberOfStops = numberOfStops,
        cost = cost,
        redirectLink = redirectLink
      )

  val validSearchFlightClientGen: Gen[SearchFlightClient] =
    Gen
      .listOf(flightGen)
      .map { flights =>
        new SearchFlightClient {
          def search(from: Airport, to: Airport, date: LocalDate): IO[List[Flight]] =
            IO(
              flights.map(
                flight =>
                  flight.copy(
                    from = from,
                    to = to,
                    departureAt = date.atTime(flight.departureAt.atOffset(ZoneOffset.UTC).toOffsetTime).toInstant
                )
              )
            )
        }
      }

  val invalidSearchFlightClientGen: Gen[SearchFlightClient] =
    Gen.listOf(flightGen).map(flights => (_: Airport, _: Airport, _: LocalDate) => IO(flights))

  val failingSearchFlightClientGen: Gen[SearchFlightClient] =
    arbitrary[Exception].map(e => (_: Airport, _: Airport, _: LocalDate) => IO.fail(e))

  val searchFlightClientGen: Gen[SearchFlightClient] =
    Gen.frequency(
      8 -> validSearchFlightClientGen,
      1 -> invalidSearchFlightClientGen,
      1 -> failingSearchFlightClientGen
    )

}
