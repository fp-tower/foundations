package answers.dataprocessing

case class Summary(min: Option[Double], max: Option[Double], sum: Double, size: Int)

object Summary {
  def one(temperature: Double): Summary =
    Summary(
      min = Some(temperature),
      max = Some(temperature),
      sum = temperature,
      size = 1,
    )

  val monoid: Monoid[Summary] = new Monoid[Summary] {
    def default: Summary = Summary(
      min = Monoid.minOption.default,
      max = Monoid.maxOption.default,
      sum = Monoid.sumDouble.default,
      size = Monoid.sumInt.default,
    )

    def combine(first: Summary, second: Summary): Summary =
      Summary(
        min = Monoid.minOption.combine(first.min, second.min),
        max = Monoid.maxOption.combine(first.max, second.max),
        sum = Monoid.sumDouble.combine(first.sum, second.sum),
        size = Monoid.sumInt.combine(first.size, second.size),
      )
  }
}
