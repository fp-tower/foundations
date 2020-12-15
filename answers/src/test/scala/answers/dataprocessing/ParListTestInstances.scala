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
        temperature <- Gen.choose(-50.0f, 150.0f)
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

  def parListGen[A](gen: Gen[A]): Gen[ParList[A]] =
    Gen
      .listOf(Gen.listOf(gen))
      .map(partitions => new ParList(global, partitions))

  implicit def parListArb[A](implicit arbA: Arbitrary[A]): Arbitrary[ParList[A]] =
    Arbitrary(parListGen(arbA.arbitrary))

  val summaryV1Gen: Gen[SummaryV1] =
    for {
      sample1 <- Arbitrary.arbitrary[Sample]
      sample2 <- Arbitrary.arbitrary[Sample]
      sum     <- Gen.choose(-10000000.0f, 10000000.0f)
      size    <- Gen.choose(0, 1000000)
      samples = List(sample1, sample2)
    } yield
      SummaryV1(samples.minByOption(_.temperatureFahrenheit), samples.maxByOption(_.temperatureFahrenheit), sum, size)

  implicit val summaryV1Arb: Arbitrary[SummaryV1] = Arbitrary(summaryV1Gen)

  val monoidIntGen: Gen[Monoid[Int]] = Gen.oneOf(
    CommutativeMonoid.sumInt,
    Monoid.multiplyInt,
    Monoid.minInt,
    Monoid.maxInt
  )

  implicit val monoidIntArb: Arbitrary[Monoid[Int]] = Arbitrary(monoidIntGen)
}
