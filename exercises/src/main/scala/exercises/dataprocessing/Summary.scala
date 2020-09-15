package exercises.dataprocessing

case class Summary(
  min: Option[Sample], // Sample with lowest temperature
  max: Option[Sample], // Sample with highest temperature
  sum: Double, // sum of all temperatures in Fahrenheit
  size: Int // number of Sample processed
) {

  def average: Option[Double] =
    Option.unless(size == 0)(sum / size)

  override def toString: String =
    f"Summary(avg = ${average.getOrElse(0.0)}%.2f, " +
      f"size = $size,\n  " +
      f"min = $min,\n  " +
      f"max = $max\n)"
}
