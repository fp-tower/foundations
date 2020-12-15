package answers.dataprocessing

case class SummaryV1(min: Option[Sample], max: Option[Sample], sum: Double, size: Int) {
  def average: Option[Double] =
    Option.unless(size == 0)(sum / size)

  override def toString: String =
    f"Summary(avg = ${average.getOrElse(0.0)}%.2f, " +
      s"size = $size,\n  " +
      s"min = $min,\n  " +
      s"max = $max\n)"
}

object SummaryV1 {
  def one(sample: Sample): SummaryV1 =
    SummaryV1(
      min = Some(sample),
      max = Some(sample),
      sum = sample.temperatureFahrenheit,
      size = 1,
    )

  def fromSummary(opt: Option[Summary]): SummaryV1 =
    opt.fold(monoid.default)(s => SummaryV1(Some(s.min), Some(s.max), s.sum, s.size))

  val monoid: Monoid[SummaryV1] = new Monoid[SummaryV1] {
    def default: SummaryV1 = SummaryV1(
      min = None,
      max = None,
      sum = 0,
      size = 0,
    )

    def combine(first: SummaryV1, second: SummaryV1): SummaryV1 =
      SummaryV1(
        min = (first.min, second.min) match {
          case (None, None)       => None
          case (Some(x), None)    => Some(x)
          case (None, Some(x))    => Some(x)
          case (Some(x), Some(y)) => Some(if (x.temperatureFahrenheit <= y.temperatureFahrenheit) x else y)
        },
        max = (first.max, second.max) match {
          case (None, None)       => None
          case (Some(x), None)    => Some(x)
          case (None, Some(x))    => Some(x)
          case (Some(x), Some(y)) => Some(if (x.temperatureFahrenheit >= y.temperatureFahrenheit) x else y)
        },
        sum = first.sum + second.sum,
        size = first.size + second.size,
      )
  }

  val monoidDerived: Monoid[SummaryV1] = new Monoid[SummaryV1] {
    val monoidMin       = Monoid.minByOption((_: Sample).temperatureFahrenheit)
    val monoidMax       = Monoid.maxByOption((_: Sample).temperatureFahrenheit)
    val monoidSumDouble = CommutativeMonoid.sumNumeric[Double]
    val monoidSumInt    = CommutativeMonoid.sumNumeric[Int]

    def default: SummaryV1 = SummaryV1(
      min = monoidMin.default,
      max = monoidMax.default,
      sum = monoidSumDouble.default,
      size = monoidSumInt.default,
    )

    def combine(first: SummaryV1, second: SummaryV1): SummaryV1 =
      SummaryV1(
        min = monoidMin.combine(first.min, second.min),
        max = monoidMax.combine(first.max, second.max),
        sum = monoidSumDouble.combine(first.sum, second.sum),
        size = monoidSumInt.combine(first.size, second.size),
      )
  }
}
