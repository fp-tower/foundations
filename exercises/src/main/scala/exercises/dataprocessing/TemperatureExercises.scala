package exercises.dataprocessing

object TemperatureExercises {
  // b. Implement `minSample` which finds the `Sample` with the smallest temperature.
  // `minSample` should work as follow:
  // Step 1: Find the local minimums (for each partition the `Sample` with the smallest temperature).
  // Step 2: Find the minimum value among the local minimums.
  def minSample(samples: ParList[Sample]): Option[Sample] =
    ???

  // c. Implement `averageTemperature` which finds the global average temperature across all
  // `Samples`. As a reminder, the average temperature is equal to the sum of all temperatures
  // divided by the number of `Samples`.
  // In case the `ParList` is empty we return `None`.
  def averageTemperature(samples: ParList[Sample]): Option[Double] =
    ???

}
