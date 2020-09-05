package answers.dataprocessing

import java.time.LocalDate

import org.scalacheck.{Arbitrary, Gen}

import scala.concurrent.ExecutionContext.global

trait ParListTestInstances {
  implicit val sampleArb: Arbitrary[Sample] =
    Arbitrary(
      for {
        (region, country, state, city) <- Gen.oneOf(
          ("Africa", "Algeria", None, "Algiers"),
          ("Africa", "Burundi", None, "Bujumbura"),
          ("Asia", "Uzbekistan", None, "Tashkent"),
          ("Asia", "Turkmenistan", None, "Ashabad"),
          ("Europe", "France", None, "Bordeaux"),
          ("Europe", "Germany", None, "Munich"),
          ("North America", "US", Some("Florida"), "Jacksonville"),
          ("North America", "US", Some("California"), "Fresno"),
        )
        minDate = LocalDate.of(1975, 1, 1)
        maxDate = LocalDate.of(2020, 1, 1)
        date        <- Gen.choose[Long](minDate.toEpochDay, maxDate.toEpochDay).map(LocalDate.ofEpochDay)
        temperature <- Gen.choose(-50.0, 150.0)
      } yield
        Sample(
          region = region,
          country = country,
          state = state,
          city = city,
          month = date.getMonthValue,
          day = date.getDayOfMonth,
          year = date.getYear,
          temperatureFahrenheit = temperature
        )
    )

  implicit def parListArb[A](implicit arbA: Arbitrary[A]): Arbitrary[ParList[A]] =
    Arbitrary(
      for {
        list              <- Gen.listOf(arbA.arbitrary)
        numberOfPartition <- Gen.choose[Int](1, 10)
      } yield ParList.byNumberOfPartition(global, numberOfPartition, list)
    )

  implicit val summaryV1Arb: Arbitrary[SummaryV1] =
    Arbitrary(
      for {
        sample1 <- Arbitrary.arbitrary[Sample]
        sample2 <- Arbitrary.arbitrary[Sample]
        sum     <- Arbitrary.arbLong.arbitrary.map(_.toDouble) // avoid decimal
        size    <- Gen.choose(0, 1000000)
        samples = List(sample1, sample2)
      } yield
        SummaryV1(samples.minByOption(_.temperatureFahrenheit), samples.maxByOption(_.temperatureFahrenheit), sum, size)
    )
}
