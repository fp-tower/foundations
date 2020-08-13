package exercises.dataprocessing

case class ParList[A](partitions: List[List[A]]) {
  def toList: List[A] =
    partitions.flatten
}

object ParList {
  def empty[A]: ParList[A] =
    ParList(Nil)

  def apply[A](partitions: List[A]*): ParList[A] =
    ParList(partitions.toList)

  // Creates a ParList by grouping a List into partitions of fixed size.
  // If the input list length is not divisible by the partition size, then
  // the last partition will be smaller. For example:
  // partition(3, List(1,2,3,4,5,6,7)) == ParList(
  //   List(1,2,3),
  //   List(4,5,6),
  //   List(7)
  // )
  def partition[A](partitionSize: Int, items: List[A]): ParList[A] =
    ParList(items.grouped(partitionSize).toList)
}
