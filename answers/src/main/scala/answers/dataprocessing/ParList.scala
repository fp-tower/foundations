package answers.dataprocessing

case class ParList[A](partitions: List[List[A]]) {
  def toList: List[A] =
    partitions.flatten

  def flatFoldLeft[B](default: B)(combine: (B, A) => B): B =
    toList.foldLeft(default)(combine)

  def splitFoldLeft[B](default: B)(combine: (A, B) => A): B =
    sys.error("Impossible")

  def splitMonoFoldLeft(default: A)(combine: (A, A) => A): A =
    partitions // List[List[A]]
      .map(_.foldLeft(default)(combine)) // List[A]
      .foldLeft(default)(combine) // A

}

object ParList {
  def apply[A](partitions: List[A]*): ParList[A] =
    ParList(partitions.toList)
}
