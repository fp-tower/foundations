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
    samples.partitions.map(_.map(_.temperatureFahrenheit).sum).sum

  def averageTemperatureOneGo(samples: ParList[Sample]): Option[Double] = {
    val (sum, length) = foldSumSizePerPartition(samples.partitions.map(sumSizeList))

    Option.unless(length == 0)(sum / length)
  }

  def sumSizeList(samples: List[Sample]): (Double, Int) =
    samples.foldLeft((0.0, 0)) {
      case ((stateSum, stateSize), sample) =>
        (stateSum + sample.temperatureFahrenheit, stateSize + 1)
    }

  def foldSumSizePerPartition(partitions: List[(Double, Int)]): (Double, Int) =
    partitions.foldLeft((0.0, 0)) {
      case ((stateSum, stateSize), (partitionSum, partitionSize)) =>
        (stateSum + partitionSum, stateSize + partitionSize)
    }

  def summaryListOnePass(samples: List[Sample]): SummaryV1 =
    samples.foldLeft(
      SummaryV1(
        min = None,
        max = None,
        sum = 0.0,
        size = 0
      )
    )(
      (state, sample) =>
        SummaryV1(
          min = state.min.fold(Some(sample))(
            current =>
              if (current.temperatureFahrenheit <= sample.temperatureFahrenheit) Some(current)
              else Some(sample)
          ),
          max = state.max.fold(Some(sample))(
            current =>
              if (current.temperatureFahrenheit >= sample.temperatureFahrenheit) Some(current)
              else Some(sample)
          ),
          sum = state.sum + sample.temperatureFahrenheit,
          size = state.size + 1
      )
    )

  def summaryList(samples: List[Sample]): SummaryV1 =
    SummaryV1(
      min = samples.minByOption(_.temperatureFahrenheit),
      max = samples.maxByOption(_.temperatureFahrenheit),
      sum = samples.foldLeft(0.0)((state, sample) => state + sample.temperatureFahrenheit),
      size = samples.size
    )

  def summaryParListOnePass(samples: ParList[Sample]): SummaryV1 =
    samples.parFoldMap(SummaryV1.one)(SummaryV1.monoidDerived)

}
