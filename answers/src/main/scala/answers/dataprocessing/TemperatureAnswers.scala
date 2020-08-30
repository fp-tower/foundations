package answers.dataprocessing

object TemperatureAnswers {

  def minSampleByTemperature(samples: ParList[Sample]): Option[Sample] =
    minSampleByTemperatureList(samples.partitions.flatMap(minSampleByTemperatureList))

  def minSampleByTemperatureList(samples: List[Sample]): Option[Sample] =
    samples.minByOption(_.temperatureFahrenheit)

  def averageTemperature(samples: ParList[Sample]): Option[Double] = {
    val length = size(samples)
    val sum    = sumTemperature(samples)
    if (length == 0) None else Some(sum / length)
  }

  def size(samples: ParList[Sample]): Int =
    samples.partitions.map(_.size).sum

  def sumTemperature(samples: ParList[Sample]): Double =
    samples.partitions.map(_.foldLeft(0.0)((state, sample) => state + sample.temperatureFahrenheit)).sum

}
