package answers.dataprocessing

case class SummaryV2(min: Double, max: Double, sum: Double, size: Int) {
  require(size > 0)

  def average: Double = sum / size

  override def toString: String =
    s"Summary(avg = ${format(average)}, min = ${format(min)}, max = ${format(max)}, points = $size)"

  private def format(number: Double): String =
    BigDecimal(number)
      .setScale(2, BigDecimal.RoundingMode.FLOOR)
      .toDouble
      .toString
}

object SummaryV2 {
  def one(temperature: Double): SummaryV2 =
    SummaryV2(
      min = temperature,
      max = temperature,
      sum = temperature,
      size = 1,
    )

  val semigroup: Semigroup[SummaryV2] = new Semigroup[SummaryV2] {
    def combine(first: SummaryV2, second: SummaryV2): SummaryV2 =
      SummaryV2(
        min = Semigroup.min[Double].combine(first.min, second.min),
        max = Semigroup.min[Double].combine(first.max, second.max),
        sum = Monoid.sumNumeric[Double].combine(first.sum, second.sum),
        size = Monoid.sumNumeric[Int].combine(first.size, second.size),
      )
  }
}
