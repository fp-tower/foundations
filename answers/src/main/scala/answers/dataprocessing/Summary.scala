package answers.dataprocessing

case class Summary(min: Sample, max: Sample, sum: Double, size: Int) {
  require(size > 0)

  def average: Double = sum / size

  override def toString: String =
    f"Summary(avg = $average%.2f, " +
      f"size = $size,\n  " +
      f"min = $min,\n  " +
      f"max = $max\n)"
}

object Summary {
  def one(sample: Sample): Summary =
    Summary(
      min = sample,
      max = sample,
      sum = sample.temperatureFahrenheit,
      size = 1,
    )

  val semigroup: Semigroup[Summary] = new Semigroup[Summary] {
    val semigroupMin    = Semigroup.minBy((_: Sample).temperatureFahrenheit)
    val semigroupMax    = Semigroup.maxBy((_: Sample).temperatureFahrenheit)
    val monoidSumDouble = CommutativeMonoid.sumNumeric[Double]
    val monoidSumInt    = CommutativeMonoid.sumNumeric[Int]

    def combine(first: Summary, second: Summary): Summary =
      Summary(
        min = if (first.min.temperatureFahrenheit <= second.min.temperatureFahrenheit) first.min else second.min,
        max = if (first.max.temperatureFahrenheit >= second.max.temperatureFahrenheit) first.max else second.max,
        sum = first.sum + second.sum,
        size = first.size + second.size,
      )
  }

  val semigroupDerived: Semigroup[Summary] = new Semigroup[Summary] {
    val semigroupMin    = Semigroup.minBy((_: Sample).temperatureFahrenheit)
    val semigroupMax    = Semigroup.maxBy((_: Sample).temperatureFahrenheit)
    val monoidSumDouble = CommutativeMonoid.sumNumeric[Double]
    val monoidSumInt    = CommutativeMonoid.sumNumeric[Int]

    def combine(first: Summary, second: Summary): Summary =
      Summary(
        min = semigroupMin.combine(first.min, second.min),
        max = semigroupMax.combine(first.max, second.max),
        sum = monoidSumDouble.combine(first.sum, second.sum),
        size = monoidSumInt.combine(first.size, second.size),
      )
  }
}
