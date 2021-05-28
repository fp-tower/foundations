## References

1. Using `Await` on `Future` is often a code smell because it blocks a `Thread`, which is a rare resource.
It is fine do this within `parFoldMap` because we expect a single parallel operation to run at a time. 
Another option would be to change `parFoldMap` to return a `Future[To]`, but it would add a bit of complexity to the implementation. (We will discuss this in chapter 5 when we talk about actions).
1. [How do fiber works?](https://www.youtube.com/watch?v=x5_MmZVLiSM) A presentation by Fabio Labella
1. Varargs [syntax](https://alvinalexander.com/scala/scala-varargs-syntax-function-examples/) and examples.
1. Scala parallel [collections](https://github.com/scala/scala-parallel-collections)

| Type         | GitHub link |
|--------------|-------------|
| Exercises    | [TemperatureNotebook.scala](https://github.com/fp-tower/foundations/blob/master/exercises/src/main/scala/exercises/dataprocessing/TemperatureNotebook.scala) |
| Tests        | [ParListTest.scala](https://github.com/fp-tower/foundations/blob/master/exercises/src/test/scala/exercises/dataprocessing/ParListTest.scala) |
| Answers      | [TemperatureNotebookAnswers.scala](https://github.com/fp-tower/foundations/blob/master/answers/src/main/scala/answers/dataprocessing/TemperatureNotebookAnswers.scala) |
| Test Answers | [ParListTest.scala](https://github.com/fp-tower/foundations/blob/master/answers/src/test/scala/answers/dataprocessing/ParListTest.scala) |
