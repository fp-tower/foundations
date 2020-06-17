package exercises.function

import java.time.{Instant, LocalDate, ZoneOffset}

import org.scalacheck.{Arbitrary, Gen}

trait ArbitraryInstances {
  implicit val localDateArbitrary: Arbitrary[LocalDate] =
    Arbitrary(
      Gen
        .choose(Instant.MIN.getEpochSecond, Instant.MAX.getEpochSecond)
        .map(Instant.ofEpochSecond)
        .map(_.atZone(ZoneOffset.UTC).toLocalDate)
    )
}
