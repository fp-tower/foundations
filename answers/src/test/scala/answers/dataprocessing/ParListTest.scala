package answers.dataprocessing

import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class ParListTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks with ParListTestInstances {

  test("minSampleByTemperature example") {
    val sample       = Sample("Africa", "Algeria", None, "Algiers", 1, 1, 2000, 0)
    val temperatures = List(1, 10, -1, 24, 18, 32, 99, 20, -34, 102, -20, 0)
    val samples      = temperatures.map(temperature => sample.copy(temperatureFahrenheit = temperature))
    val parSamples   = ParList.byNumberOfPartition(3, samples)

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
      assert(numbers.monoFoldLeft(0)(_ + _) == numbers.toList.sum)
    }
  }

  ignore("monoFoldLeft consistent with List foldLeft (not true)") {
    forAll { (numbers: ParList[Int], default: Int, combine: (Int, Int) => Int) =>
      assert(numbers.monoFoldLeft(default)(combine) == numbers.toList.foldLeft(default)(combine))
    }
  }

}
