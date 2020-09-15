package answers.dataprocessing

import Ordering.Double.TotalOrdering
import org.scalacheck.Arbitrary
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import scala.concurrent.ExecutionContext.global

class ParListTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks with ParListTestInstances {

  test("minSampleByTemperature example") {
    val sample       = Sample("Africa", "Algeria", None, "Algiers", 1, 1, 2000, 0)
    val temperatures = List(1, 10, -1, 24, 18, 32, 99, 20, -34, 102, -20, 0)
    val samples      = temperatures.map(temperature => sample.copy(temperatureFahrenheit = temperature))
    val parSamples   = ParList.byNumberOfPartition(global, 3, samples)

    assert(
      TemperatureAnswers.minSampleByTemperature(parSamples) ==
        Some(Sample("Africa", "Algeria", None, "Algiers", 1, 1, 2000, -34))
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

//  checkMonoid("Sum Double", Monoid.sumNumeric[Double]) not stable
  checkMonoid("Sum Int", Monoid.sumNumeric[Int])
  checkMonoid("Max Option[Int]", Monoid.maxOption[Int])
  checkMonoid("Min Option[Int]", Monoid.minOption[Int])
//  checkMonoid("SummaryV1", SummaryV1.monoid) TODO check
  checkMonoid("Map[String, Int]", Monoid.map[String, Int](Monoid.sumNumeric))

  test("foldMap consistent with map + monoFoldMap") {
    forAll { (numbers: ParList[Int]) =>
      val monoid = Monoid.sumNumeric[Int]
      assert(numbers.fold(monoid) == numbers.monoFoldLeft(monoid))
    }
  }

  test("summary consistent between implementations") {
    forAll { (samples: ParList[Sample]) =>
      val list           = TemperatureAnswers.summaryList(samples.toList)
      val listOnePass    = TemperatureAnswers.summaryListOnePass(samples.toList)
      val parListOnePass = TemperatureAnswers.summaryParListOnePass(samples)

      assert(list == listOnePass)

      assert(list.size == parListOnePass.size)
      assert((list.sum - parListOnePass.sum).abs <= 0.001)
      assert(list.min == parListOnePass.min)
      assert(list.max == parListOnePass.max)
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

  def checkMonoid[A: Arbitrary](name: String, monoid: Monoid[A]) = {
    test(s"$name Monoid default is a noop") {
      forAll { (value: A) =>
        assert(monoid.combine(value, monoid.default) == value)
        assert(monoid.combine(monoid.default, value) == value)
      }
    }
    test(s"$name Monoid combine is associative") {
      forAll { (first: A, second: A, third: A) =>
        assert(
          monoid.combine(first, monoid.combine(second, third)) ==
            monoid.combine(monoid.combine(first, second), third)
        )
      }
    }
  }

}
