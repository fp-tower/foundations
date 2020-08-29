package exercises.dataprocessing

case class ParList[A](partitions: List[List[A]]) {
  // d. Implement `foldLeft` and then move it to be a method of the class `ParList`.
  // `foldLeft` should work as follow:
  // Step 1: Fold each partition into a single value.
  // Step 2: Fold the results of all partitions together.
  // For example,
  // Partition 1: List(a1, b1, c1, d1, e1, f1) ->       x   (folded partition 1)  \
  // Partition 2: List(a2, b2, c2, d2, e2, f2) ->       y   (folded partition 2) - z (final result)
  // Partition 3:                          Nil -> default (partition 3 is empty)  /
  def foldLeft[To](default: To)(combine: (To, A) => To): To =
    ???

  // e. Implement `monoFoldLeft`, a version of `foldLeft` that does not change the element type.
  // `monoFoldLeft` should work as follow:
  // Step 1: Fold each partition into a single value.
  // Step 2: Fold the results of all partitions together.
  // For example,
  // Partition 1: List(a1, b1, c1, d1, e1, f1) ->       x   (folded partition 1)  \
  // Partition 2: List(a2, b2, c2, d2, e2, f2) ->       y   (folded partition 2) - z (final result)
  // Partition 3:                          Nil -> default (partition 3 is empty)  /
  def monoFoldLeft(default: A)(combine: (A, A) => A): A =
    ???
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
