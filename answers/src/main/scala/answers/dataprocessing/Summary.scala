package answers.dataprocessing

case class Summary(min: Double, max: Double, sum: Double, size: Int) {
  require(size > 0)

  def average: Double = sum / size

  override def toString: String =
    f"Summary(avg = $average%.2f, min = $min%.2f, max = $max%.2f, points = $size)"
}

object Summary {
  def one(temperature: Double): Summary =
    Summary(
      min = temperature,
      max = temperature,
      sum = temperature,
      size = 1,
    )

  val semigroup: Semigroup[Summary] = new Semigroup[Summary] {
    def combine(first: Summary, second: Summary): Summary =
      Summary(
        min = Semigroup.min[Double].combine(first.min, second.min),
        max = Semigroup.min[Double].combine(first.max, second.max),
        sum = Monoid.sumNumeric[Double].combine(first.sum, second.sum),
        size = Monoid.sumNumeric[Int].combine(first.size, second.size),
      )
  }
}
