package exercises.dataprocessing

object TemperatureExercises {
  // b. Implement `minSampleByTemperature` which finds the `Sample` with the coldest temperature.
  // `minSampleByTemperature` should work as follow:
  // Step 1: Find the local minimums (for each partition the `Sample` with the coldest temperature).
  // Step 2: Find the minimum value among the local minimums.
  def minSampleByTemperature(samples: ParList[Sample]): Option[Sample] =
    ???

  // c. Implement `averageTemperature` which finds the average temperature across all `Samples`.
  // `averageTemperature` should work as follow:
  // Step 1: Compute the size each partition.
  // Step 2: Sum-up the size of all partitions, this gives the size for the entire `ParList`.
  // Step 3: Compute the sum of temperatures for each partition.
  // Step 4: Sum-up the resulting number for all partitions, this gives the total temperature for the entire `ParList`.
  // Step 5: Divide total temperature by the size of dataset.
  // In case the input `ParList` is empty we return `None`.
  // Can you calculate the size and sum in one go?
  def averageTemperature(samples: ParList[Sample]): Option[Double] =
    ???

  // d. Implement `foldLeft` and then move it inside the class `ParList`.
  // `foldLeft` should work as follow:
  // Step 1: Fold each partition into a single value.
  // Step 2: Fold the intermediate results of all partitions together.
  // For example,
  // Partition 1: List(a1, b1, c1, d1, e1, f1) ->    res1 (intermediate result of partition 1) \
  // Partition 2: List(a2, b2, c2, d2, e2, f2) ->    res2 (intermediate result of partition 2) - finalResult
  // Partition 3:                          Nil -> default (partition 3 is empty)               /
  def foldLeft[From, To](parList: ParList[From], default: To)(combine: (To, From) => To): To =
    ???

  // e. Implement `monoFoldLeft`, a version of `foldLeft` that does not change the element type.
  // Then move `monoFoldLeft` inside  the class `ParList`.
  // `monoFoldLeft` should work as follow:
  // Step 1: Fold each partition into a single value.
  // Step 2: Fold the results of all partitions together.
  // For example,
  // Partition 1: List(a1, b1, c1, d1, e1, f1) ->       x   (folded partition 1)  \
  // Partition 2: List(a2, b2, c2, d2, e2, f2) ->       y   (folded partition 2) - z (final result)
  // Partition 3:                          Nil -> default (partition 3 is empty)  /
  def monoFoldLeft[A](parList: ParList[A], default: A)(combine: (A, A) => A): A =
    ???

  // f. Implement `map`, you should know what it does by now ;)
  // Then move `map` inside  the class `ParList`.
  // Finally, refactor `minSampleByTemperature` to use a combination of `monoFoldLeft` and `map`.
  def map[From, To](parList: ParList[From])(update: From => To): ParList[To] =
    ???

  // g. Refactor `minBy`, `maxBy` and `averageTemperature` to use `map` and `monoFoldLeft`

  // h. Implement a new folding method on `ParList` that combines both `map` and `monoFoldLeft`
  // together such that we only iterate over the dataset once.
  // Then refactor `minBy`, `maxBy` and `averageTemperature` to use it.

  // i. Implement a version of the function implemented in h) such that each partition is
  // processed in parallel.
  // Then refactor `minBy`, `maxBy` and `averageTemperature` to use it.
  // Next question is in  benchmark section of `TemperatureNotebook`.

  // Implement `summaryList` using List `foldLeft`.
  // Calculate `min`, `max`, `average`, `sum` by iterating over `samples` only ONCE.
  def summaryListOnePass(samples: List[Sample]): Summary =
    ???

  // `summaryList` iterate 4 times over `samples`, one for each field.
  def summaryList(samples: List[Sample]): Summary =
    Summary(
      min = samples.minByOption(_.temperatureFahrenheit),
      max = samples.maxByOption(_.temperatureFahrenheit),
      sum = samples.foldLeft(0.0)((state, sample) => state + sample.temperatureFahrenheit),
      size = samples.size
    )

  // Implement `summaryParListOnePass` using `parFoldMap`.
  // Calculate `min`, `max`, `average`, `sum` by iterating over `samples` only ONCE.
  def summaryParListOnePass(samples: ParList[Sample]): Summary =
    ???
}
