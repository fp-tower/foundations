package answers.dataprocessing

case class SummaryV1(min: Option[Double], max: Option[Double], sum: Double, size: Int) {
  def average: Double = sum / size

  override def toString: String =
    s"Summary(avg = ${format(average)}, min = ${min.map(format)}, max = ${max.map(format)}, points = $size)"

  private def format(number: Double): String =
    BigDecimal(number)
      .setScale(2, BigDecimal.RoundingMode.FLOOR)
      .toDouble
      .toString
}

object SummaryV1 {
  def one(temperature: Double): SummaryV1 =
    SummaryV1(
      min = Some(temperature),
      max = Some(temperature),
      sum = temperature,
      size = 1,
    )

  def fromSummary(opt: Option[Summary]): SummaryV1 =
    opt.fold(monoid.default)(s => SummaryV1(Some(s.min), Some(s.max), s.sum, s.size))

  val monoid: Monoid[SummaryV1] = new Monoid[SummaryV1] {
    def default: SummaryV1 = SummaryV1(
      min = Monoid.minOption[Double].default,
      max = Monoid.maxOption[Double].default,
      sum = Monoid.sumNumeric[Double].default,
      size = Monoid.sumNumeric[Int].default,
    )

    def combine(first: SummaryV1, second: SummaryV1): SummaryV1 =
      SummaryV1(
        min = Monoid.minOption[Double].combine(first.min, second.min),
        max = Monoid.maxOption[Double].combine(first.max, second.max),
        sum = Monoid.sumNumeric[Double].combine(first.sum, second.sum),
        size = Monoid.sumNumeric[Int].combine(first.size, second.size),
      )
  }
}
