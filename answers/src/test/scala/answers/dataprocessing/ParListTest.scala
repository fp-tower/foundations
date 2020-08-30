package answers.dataprocessing

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class ParListTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("minSampleByTemperature") {
    forAll { (samples: ParList[Sample]) =>
      assert(
        TemperatureAnswers.minSampleByTemperature(samples) ==
          samples.toList.minByOption(_.temperatureCelsius)
      )
    }
  }

  test("min") {
    forAll { (numbers: ParList[Int]) =>
      assert(numbers.min == numbers.toList.minOption)
    }
  }

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
        year        <- Gen.choose(1980, 2020)
        month       <- Gen.choose(1, 12)
        day         <- Gen.choose(1, 28)
        temperature <- Gen.choose[Double](-50, 150)
      } yield
        Sample(
          region = region,
          country = country,
          state = state,
          city = city,
          month = month,
          day = day,
          year = year,
          temperatureFahrenheit = temperature
        )
    )

  implicit def parListArb[A](implicit arbA: Arbitrary[A]): Arbitrary[ParList[A]] =
    Arbitrary(
      for {
        list              <- Gen.listOf(arbA.arbitrary)
        numberOfPartition <- Gen.choose[Int](1, 10)
      } yield ParList.byNumberOfPartition(numberOfPartition, list)
    )

}
