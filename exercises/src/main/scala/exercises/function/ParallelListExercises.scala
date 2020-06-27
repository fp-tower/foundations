package exercises.function

object ParallelListExercises {
  ////////////////////////
  // Exercise 1: ParList
  ////////////////////////

  case class ParList[A](partitions: List[List[A]]) {
    def flat: List[A] = partitions.flatten

    def isEmpty: Boolean =
      partitions.isEmpty || partitions.forall(_.isEmpty)

    def map[To](update: A => To): ParList[To] =
      ParList(partitions.map(_.map(update)))

    // 1a. Implement `flatFoldLeft` such as it behaves as if
    def flatFoldLeft[To](default: To)(combine: (To, A) => To): To =
      ???

    // 1b. Implement `monoFoldLeft` such that each partition is folded separately
    // then all aggregate are folded together, e.g.
    // ParList(
    //   List(1,2,3),  // fold 1st partition into 6
    //   List(4,5,6),  // fold 2nd partition into 15
    //   List(7, 8)    // fold 3rd partition into 15
    // ).monoFoldLeft(0)(_ + _)
    // Then fold List(6, 15, 15) into 36.
    // Note: `monoFoldLeft` should return the same result as `flatFoldLeft`.
    def monoFoldLeft(default: A)(combine: (A, A) => A): A =
      ???

    // 1c. Implement `foldLeft`, a generalisation of `monoFoldLeft` where the default
    // and output type is not necessarily the same as the input type of `ParList`.
    // `foldLeft` should fold each partition separately and then fold all aggregates together.
    // Note: `foldLeft` should return the same result as `flatFoldLeft`.
    def foldLeft[To](default: A)(combine: (To, A) => To): To =
      ???

    // 1d. Implement `reduceLeft`, a generalisation of `monoFoldLeft` where the default
    // if `isEmpty == true`  then `reduceLeft(combine) == None`
    // if `isEmpty == false` then `reduceLeft(_ + _) == Some(monoFoldLeft(0)(_ + _))`
    def reduceLeft(combine: (A, A) => A): Option[A] =
      ???

  }
}
