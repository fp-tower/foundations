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

  def sumTemperatureV2(samples: ParList[Sample]): Double =
    samples.foldLeftV2(0.0)((state, sample) => state + sample.temperatureFahrenheit)(_ + _)

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

  def summaryList(samples: List[Sample]): SummaryV1 =
    SummaryV1(
      min = samples.minByOption(_.temperatureFahrenheit),
      max = samples.maxByOption(_.temperatureFahrenheit),
      sum = samples.foldLeft(0.0)((state, sample) => state + sample.temperatureFahrenheit),
      size = samples.size
    )

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

  def summaryParList(samples: ParList[Sample]): SummaryV1 =
    SummaryV1(
      min = samples.minBy(_.temperatureFahrenheit),
      max = samples.maxBy(_.temperatureFahrenheit),
      sum = samples.foldMap(_.temperatureFahrenheit)(CommutativeMonoid.sumNumeric),
      size = samples.size
    )

  def summaryParListOnePassFoldMap(samples: ParList[Sample]): SummaryV1 =
    samples.parFoldMap(SummaryV1.one)(SummaryV1.monoid)

  def summaryParListOnePassReduceMap(samples: ParList[Sample]): SummaryV1 =
    SummaryV1.fromSummary(samples.parReduceMap(Summary.one)(Summary.semigroup))

}
