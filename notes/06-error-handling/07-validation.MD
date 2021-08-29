| Type         | GitHub link |
|--------------|-------------|
| Exercises    |     [ValidationExercises.scala](https://github.com/fp-tower/foundations/blob/master/exercises/src/main/scala/exercises/errorhandling/validation/ValidationExercises.scala)       |
| Tests        | [ValidationExercisesTest.scala](https://github.com/fp-tower/foundations/blob/master/exercises/src/test/scala/exercises/errorhandling/validation/ValidationExercisesTest.scala)   |
| Answers      |       [ValidationAnswers.scala](https://github.com/fp-tower/foundations/blob/master/answers/src/main/scala/answers/errorhandling/validation/ValidationAnswers.scala)     |
| Test Answers |   [ValidationAnswersTest.scala](https://github.com/fp-tower/foundations/blob/master/answers/src/test/scala/answers/errorhandling/validation/ValidationAnswersTest.scala) |

# References

* [Validated](https://typelevel.org/cats/datatypes/validated.html) in [cats](https://github.com/typelevel/cats)
* [ZValidation](https://github.com/zio/zio-prelude/blob/master/core/shared/src/main/scala/zio/prelude/ZValidation.scala) in [zio-prelude](https://github.com/zio/zio-prelude)
* Cats and zio-prelude generalises the Invalid scenario to data types other than NonEmptyList. Instead, they require the error type E to be a Semigroup (see end of chapter 3). Essentially, they accept any E as long as we have a method `combine(e1: E, e2: E): E`. Typically, `combine` corresponds to the method  `++` when E is a data structure.
* [Advanced] Use Either to accumulate errors with cats [parallel](https://typelevel.org/cats/typeclasses/parallel.html).
