package exercises.dataprocessing

import java.time.LocalDate

import org.scalacheck.{Arbitrary, Gen}

trait ParListTestInstances {
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
            ("North America", "US", Some("California"), "Fresno")
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
          partitionSize = math.ceil(list.length / numberOfPartition.toDouble).toInt
        } yield ParList.byPartitionSize(partitionSize, list)
      )
  }
}
