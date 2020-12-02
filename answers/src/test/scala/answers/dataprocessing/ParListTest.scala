package answers.dataprocessing

import Ordering.Double.TotalOrdering
import org.scalacheck.Arbitrary
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.concurrent.ExecutionContext.global
import TemperatureAnswers._
import org.scalactic.{Equality, TolerantNumerics}

class ParListTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks with ParListTestInstances {

  test("minSampleByTemperature example") {
    val samples = List(
      Sample("Africa", "Algeria", None, "Algiers", 8, 1, 2020, 50),
      Sample("Africa", "Algeria", None, "Algiers", 8, 1, 2020, 56.3),
      Sample("Africa", "Algeria", None, "Algiers", 8, 1, 2020, 23.4),
      Sample("Africa", "Algeria", None, "Algiers", 8, 1, 2020, 89.7),
      Sample("Africa", "Algeria", None, "Algiers", 8, 1, 2020, 22.1),
      Sample("Africa", "Algeria", None, "Algiers", 8, 1, 2020, 34.7),
      Sample("Africa", "Algeria", None, "Algiers", 8, 1, 2020, 99.0),
    )
    val parSamples = ParList.byPartitionSize(global, 3, samples)

    assert(
      minSampleByTemperature(parSamples) ==
        Some(Sample("Africa", "Algeria", None, "Algiers", 8, 1, 2020, 22.1))
    )
  }

  test("minSampleByTemperature consistent with List minByOption") {
    forAll { (samples: ParList[Sample]) =>
      assert(
        TemperatureAnswers.minSampleByTemperature(samples) ==
          samples.toList.minByOption(_.temperatureCelsius)
      )
    }
  }

  test("min with List minOption") {
    forAll { (numbers: ParList[Int]) =>
      assert(numbers.min == numbers.toList.minOption)
    }
  }

  test("size") {
    forAll { (numbers: ParList[Int]) =>
      assert(numbers.size == numbers.toList.size)
    }
  }

  test("averageTemperature example") {
    val samples = List(
      Sample("Africa", "Algeria", None, "Algiers", 8, 1, 2020, 50),
      Sample("Africa", "Algeria", None, "Algiers", 8, 1, 2020, 56.3),
      Sample("Africa", "Algeria", None, "Algiers", 8, 1, 2020, 23.4),
      Sample("Africa", "Algeria", None, "Algiers", 8, 1, 2020, 89.7),
      Sample("Africa", "Algeria", None, "Algiers", 8, 1, 2020, 22.1),
      Sample("Africa", "Algeria", None, "Algiers", 8, 1, 2020, 34.7),
      Sample("Africa", "Algeria", None, "Algiers", 8, 1, 2020, 99.0),
    )
    val parSamples = ParList.byPartitionSize(global, 3, samples)

    assert(averageTemperature(parSamples) == Some(53.6))
  }

  test("averageTemperature: min <= avg <= max ") {
    forAll { (samples: ParList[Sample]) =>
      val optMin = samples.minBy(_.temperatureFahrenheit).map(_.temperatureFahrenheit)
      val optMax = samples.maxBy(_.temperatureFahrenheit).map(_.temperatureFahrenheit)
      val optAvg = TemperatureAnswers.averageTemperature(samples)

      (optMin, optMax, optAvg) match {
        case (None, None, None) => succeed
        case (Some(min), Some(max), Some(avg)) =>
          assert(min <= avg)
          assert(avg <= max)
        case _ => fail(s"inconsistent $optMin, $optMax, $optAvg")
      }
    }
  }

  test("averageTemperature: avg(x - avg) = 0 ") {
    forAll { (samples: ParList[Sample]) =>
      TemperatureAnswers.averageTemperature(samples) match {
        case None => succeed
        case Some(avg) =>
          val updated = samples.map(sample => sample.copy(temperatureFahrenheit = sample.temperatureFahrenheit - avg))
          TemperatureAnswers.averageTemperature(updated) match {
            case None            => fail("updated average shouldn't be None")
            case Some(updateAvg) => assert(updateAvg.abs <= 0.0001)
          }
      }
    }
  }

  test("monoFoldLeft consistent with List sum") {
    forAll { (numbers: ParList[Int]) =>
      assert(numbers.monoFoldLeftV1(0)(_ + _) == numbers.toList.sum)
    }
  }

  ignore("monoFoldLeft consistent with List foldLeft (not true)") {
    forAll { (numbers: ParList[Int], default: Int, combine: (Int, Int) => Int) =>
      assert(numbers.monoFoldLeftV1(default)(combine) == numbers.toList.foldLeft(default)(combine))
    }
  }

  implicit val doubleEq: Equality[Double] = TolerantNumerics.tolerantDoubleEquality(0.0001)

  checkMonoid("Sum Double", Monoid.sumNumeric[Double])
  checkMonoid("Max Option[Int]", Monoid.maxOption[Int])
  checkMonoid("Min Option[Int]", Monoid.minOption[Int])
//  checkMonoid("SummaryV1", SummaryV1.monoid)
  checkMonoid("Map[String, Int]", Monoid.map[String, Int](Monoid.sumNumeric))

  test("foldMap consistent with map + monoFoldMap") {
    forAll { (numbers: ParList[Int]) =>
      val monoid = Monoid.sumNumeric[Int]
      assert(numbers.fold(monoid) == numbers.monoFoldLeft(monoid))
    }
  }

  test("summary consistent between implementations") {
    forAll { (samples: ParList[Sample]) =>
      val reference = TemperatureAnswers.summaryList(samples.toList)
      List(
        TemperatureAnswers.summaryListOnePass(samples.toList),
        TemperatureAnswers.summaryParList(samples),
        TemperatureAnswers.summaryParListOnePassFoldMap(samples),
        TemperatureAnswers.summaryParListOnePassReduceMap(samples),
      ).foreach { other =>
        assert(reference.size == other.size)
        assert((reference.sum - other.sum).abs < 0.00001)
        assert(reference.min == other.min)
        assert(reference.max == other.max)
      }
    }
  }

  test("map Monoid example") {
    assert(
      Monoid
        .map[String, Int](Monoid.sumNumeric)
        .combine(
          Map("Bob"   -> 2, "Eda" -> 5),
          Map("Roger" -> 1, "Eda" -> 2)
        ) == Map("Bob" -> 2, "Roger" -> 1, "Eda" -> 7)
    )
  }

  def checkMonoid[A: Arbitrary: Equality](name: String, monoid: Monoid[A]) = {
    test(s"$name Monoid default is a noop") {
      forAll { (value: A) =>
        assert(monoid.combine(value, monoid.default) === value)
        assert(monoid.combine(monoid.default, value) === value)
      }
    }
    test(s"$name Monoid combine is associative") {
      forAll { (first: A, second: A, third: A) =>
        assert(
          monoid.combine(first, monoid.combine(second, third)) ===
            monoid.combine(monoid.combine(first, second), third)
        )
      }
    }
  }

}
