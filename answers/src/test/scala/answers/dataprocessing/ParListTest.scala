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

}
