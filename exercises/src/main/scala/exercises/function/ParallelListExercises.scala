package exercises.function

object ParallelListExercises {
  ////////////////////////
  // Exercise 1: ParList
  ////////////////////////

  object ParList {
    // Creates a ParList by partitioning a List into partitions of fixed size
    // such partition(3, List(1,2,3,4,5,6,7,8)) == ParList(
    //   List(1,2,3),
    //   List(4,5,6),
    //   List(7, 8)
    // )
    def partition[A](partitionSize: Int, items: List[A]): ParList[A] =
      ParList(items.grouped(partitionSize).toList)
  }

  case class ParList[A](partitions: List[List[A]]) {
    def flat: List[A] =
      partitions.flatten

    def isEmpty: Boolean =
      partitions.isEmpty || // no partition or
        partitions.forall(_.isEmpty) // all partitions are empty

    // Squashes all partitions together and then use `foldLeft`
    def flatFoldLeft[To](default: To)(combine: (To, A) => To): To =
      flat.foldLeft(default)(combine)

    // 1a. Implement `monoFoldLeft` such that it folds each partition separately
    // then it folds all intermediate results together, e.g.
    // ParList(
    //   List(1,2,3),  // fold 1st partition into 6
    //   List(4,5,6),  // fold 2nd partition into 15
    //   List(7, 8)    // fold 3rd partition into 15
    // ).monoFoldLeft(0)(_ + _)
    // Then fold List(6, 15, 15) into 36.
    // Note: `monoFoldLeft` should return the same results as `flatFoldLeft`.
    def monoFoldLeft(default: A)(combine: (A, A) => A): A =
      ???

    // 1d. Implement `foldLeft`, a generalisation of `monoFoldLeft` where the output
    // type is not necessarily the same type as the element type of `ParList`.
    // `foldLeft` should fold each partition separately and then fold all intermediate results together.
    // Note: `foldLeft` should return the same results as `flatFoldLeft`.
    def foldLeft[To](default: A)(combine: (To, A) => To): To =
      ???

    // 1e. Implement `reduceLeft`, a generalisation of `monoFoldLeft` where the default
    // if `isEmpty == true`  then `reduceLeft(combine) == None`
    // if `isEmpty == false` then `reduceLeft(_ + _) == Some(monoFoldLeft(0)(_ + _))`
    def reduceLeft(combine: (A, A) => A): Option[A] =
      ???

    def map[To](update: A => To): ParList[To] =
      ParList(partitions.map(_.map(update)))
  }

  // 1b. Implement `sumInt` which sums up all the numbers in a ParList
  // such as sumInt(ParList(List(1,2,3), List(4,5))) == 15
  // Note: try to use `monoFoldLeft`
  def sumInt(parList: ParList[Int]): Int =
    ???

  // 1c. Implement `maxTemperature` which compute the highest recorded temperature
  // per city such as maxTemperature(ParList(
  //   List(TemperatureRecording("London", 19), TemperatureRecording("Paris" , 32), TemperatureRecording("Paris", 18)),
  //   List(TemperatureRecording("Paris" , 16), TemperatureRecording("London", 23))
  // )) == Map("London" -> 23, "Paris" -> 32)
  // Note: try to you use `monoFoldLeft`
  case class TemperatureRecording(city: String, temperature: Int)

  def maxTemperature(parList: ParList[TemperatureRecording]): Map[String, Int] =
    ???

}
