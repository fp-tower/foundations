package exercises.dataprocessing

object TemperatureExercises {
  // b. Implement `minSampleByTemperature` which finds the `Sample` with the coldest temperature.
  // `minSampleByTemperature` should work as follow:
  // Step 1: Find the local minimums (for each partition the `Sample` with the coldest temperature).
  // Step 2: Find the minimum value among the local minimums.
  def minSampleByTemperature(samples: ParList[Sample]): Option[Sample] =
    ???

  // c. Implement `averageTemperature` which finds the global average temperature across all
  // `Samples`. As a reminder, the average temperature is equal to the sum of all temperatures
  // divided by the number of `Samples`.
  // In case the `ParList` is empty we return `None`.
  def averageTemperature(samples: ParList[Sample]): Option[Double] =
    ???

  // d. Implement `foldLeft` and then move it to be a method of the class `ParList`.
  // `foldLeft` should work as follow:
  // Step 1: Fold each partition into a single value.
  // Step 2: Fold the results of all partitions together.
  // For example,
  // Partition 1: List(a1, b1, c1, d1, e1, f1) ->       x   (folded partition 1)  \
  // Partition 2: List(a2, b2, c2, d2, e2, f2) ->       y   (folded partition 2) - z (final result)
  // Partition 3:                          Nil -> default (partition 3 is empty)  /
  def foldLeft[From, To](default: To)(combine: (To, From) => To): To =
    ???

  // e. Implement `monoFoldLeft`, a version of `foldLeft` that does not change the element type.
  // `monoFoldLeft` should work as follow:
  // Step 1: Fold each partition into a single value.
  // Step 2: Fold the results of all partitions together.
  // For example,
  // Partition 1: List(a1, b1, c1, d1, e1, f1) ->       x   (folded partition 1)  \
  // Partition 2: List(a2, b2, c2, d2, e2, f2) ->       y   (folded partition 2) - z (final result)
  // Partition 3:                          Nil -> default (partition 3 is empty)  /
  def monoFoldLeft[A](default: A)(combine: (A, A) => A): A =
    ???
}
