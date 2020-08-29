package answers.dataprocessing

object TemperatureAnswers {

  def minSample(samples: ParList[Sample]): Option[Sample] =
    minSampleList(samples.partitions.flatMap(minSampleList))

  def minSampleList(samples: List[Sample]): Option[Sample] =
    samples.minByOption(_.temperature)

  def averageTemperature(samples: ParList[Sample]): Option[Double] = {
    val length = size(samples)
    val sum    = sumTemperature(samples)
    if (length == 0) None else Some(sum / length)
  }

  def size(samples: ParList[Sample]): Int =
    samples.partitions.map(_.size).sum

  def sumTemperature(samples: ParList[Sample]): Double =
    samples.partitions.map(_.foldLeft(0.0)((state, sample) => state + sample.temperature)).sum

  def foldLeft[From, To](elements: ParList[From], default: To)(combine: (To, From) => To): To =
    sys.error("Impossible to implement")

  def monoFoldLeft[A](elements: ParList[A], default: A)(combine: (A, A) => A): A =
    elements.partitions
      .map(_.foldLeft(default)(combine))
      .foldLeft(default)(combine)

}
