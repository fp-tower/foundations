package answers.dataprocessing

object ParallelAnswers {

  val ages: ParList[Int] = ParList(
    List(8, 3, 24, 89, 56, 32, 12, 9, 24, 56, 34),
    List(23, 24),
    List(80, 79, 64, 57, 99),
    List(30)
  )

  def sum(numbers: ParList[Int]): Int =
    numbers.partitions
      .foldLeft(0)((acc, partition) => acc + partition.sum)

  def max(numbers: ParList[Int]): Option[Int] =
    numbers.partitions
      .foldLeft(Option.empty[Int])(
        (acc, partition) => maxOption(acc, partition.maxOption)
      )

  def maxOption(optFirst: Option[Int], optSecond: Option[Int]): Option[Int] =
    combineOption(optFirst, optSecond)(_ max _)

  def combineOption[A](optFirst: Option[A], optSecond: Option[A])(combine: (A, A) => A): Option[A] =
    (optFirst, optSecond) match {
      case (Some(first), Some(second)) => Some(combine(first, second))
      case (Some(first), None)         => Some(first)
      case (None, Some(second))        => Some(second)
      case (None, None)                => None
    }

}
