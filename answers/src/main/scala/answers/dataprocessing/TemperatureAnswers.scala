package answers.dataprocessing

object TemperatureAnswers {

  def minSampleByTemperature(samples: ParList[Sample]): Option[Sample] =
    minSampleByTemperatureList(samples.partitions.flatMap(minSampleByTemperatureList))

  def minSampleByTemperatureList(samples: List[Sample]): Option[Sample] =
    samples.minByOption(_.temperatureFahrenheit)

  def averageTemperature(samples: ParList[Sample]): Option[Double] = {
    val length = size(samples)
    val sum    = sumTemperature(samples)
    Option.unless(length == 0)(sum / length)
  }

  def size(samples: ParList[Sample]): Int =
    samples.partitions.map(_.size).sum

  def sumTemperature(samples: ParList[Sample]): Double =
    samples.partitions.flatMap(_.map(_.temperatureFahrenheit)).sum

  def averageTemperatureV2(samples: ParList[Sample]): Option[Double] = {
    val (length, sum) = samples.partitions
      .map(
        partition =>
          partition.foldLeft((0, 0.0)) {
            case ((size, total), sample) =>
              (size + 1, total + sample.temperatureFahrenheit)
        }
      )
      .foldLeft((0, 0.0)) {
        case ((size1, total1), (size2, total2)) =>
          (size1 + size2, total1 + total2)
      }
    Option.unless(length == 0)(sum / length)
  }

}
