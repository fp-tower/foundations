## References

1. Scala 3 main [methods](https://dotty.epfl.ch/docs/reference/changed-features/main-functions.html)
1. A major drawback of `ParList` is that we load the entire dataset into memory before starting the analysis. 
A much better solution would stream the rows from file and analyse it on the fly. However, this would complicate 
significantly this exercise which is already quite advanced. 
1. If you have limited memory, you may not be able to load the entire CSV into memory (around 140 MB). 
If that’s the case, you can truncate the number of rows read from the dataset by adding `take(x)` 
in between `reader.toList` in the file `TemperatureNotebook.scala`:
```scala
val rows: List[Either[ReadError, Sample]] = reader.take(10000).toList
```
However, you may get different results that the ones in the video since you don't analyse the entire dataset.


| Type         | GitHub link |
|--------------|-------------|
| Exercises    | [TemperatureNotebook.scala](https://github.com/fp-tower/foundations/blob/master/exercises/src/main/scala/exercises/dataprocessing/TemperatureNotebook.scala) |
| Tests        | [ParListTest.scala](https://github.com/fp-tower/foundations/blob/master/exercises/src/test/scala/exercises/dataprocessing/ParListTest.scala) |
| Answers      | [TemperatureNotebookAnswers.scala](https://github.com/fp-tower/foundations/blob/master/answers/src/main/scala/answers/dataprocessing/TemperatureNotebookAnswers.scala) |
| Test Answers | [ParListTest.scala](https://github.com/fp-tower/foundations/blob/master/answers/src/test/scala/answers/dataprocessing/ParListTest.scala) |