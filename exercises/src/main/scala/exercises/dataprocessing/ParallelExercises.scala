package exercises.dataprocessing

object ParallelExercises {

  // a. Implement `sum` which sums up all the numbers in each partition
  // and then, sums up all the intermediate results. For example,
  // sum(ParList(
  //   List(1,2,3),  // sums up 1st partition which gives 6
  //   List(4,5,6),  // sums up 2nd partition which gives 15
  //   List(7,8)     // sums up 3rd partition which gives 15
  // ) == 36         // the sum of all intermediate results
  def sum(parList: ParList[Int]): Int =
    ???

  // b. Implement `max` which calculate the highest number in each partition
  // and then, the max of all the intermediate results. For example,
  // max(ParList(
  //   List(10,2,4),  // max of 1st partition is 10
  //   List(-3,5,2),  // max of 2nd partition is 5
  //   List(22,10)    // max of 3rd partition is 22
  // ) == Some(22)    // the max of all intermediate
  // but max(ParList.empty) == None
  def max(parList: ParList[Int]): Option[Int] =
    ???

}
