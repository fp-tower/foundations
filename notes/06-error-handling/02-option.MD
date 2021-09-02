| Type         | GitHub link |
|--------------|-------------|
| Exercises    |     [OptionExercises.scala](https://github.com/fp-tower/foundations/blob/master/exercises/src/main/scala/exercises/errorhandling/option/OptionExercises.scala)       |
| Tests        | [OptionExercisesTest.scala](https://github.com/fp-tower/foundations/blob/master/exercises/src/test/scala/exercises/errorhandling/option/OptionExercisesTest.scala)   |
| Answers      |       [OptionAnswers.scala](https://github.com/fp-tower/foundations/blob/master/answers/src/main/scala/answers/errorhandling/option/OptionAnswers.scala)     |
| Test Answers |   [OptionAnswersTest.scala](https://github.com/fp-tower/foundations/blob/master/answers/src/test/scala/answers/errorhandling/option/OptionAnswersTest.scala) |

## Sequence and Traverse

`sequence` and `traverse` come from a functional abstraction called [Traverse](https://typelevel.org/cats/typeclasses/traverse.html) in [cats](https://typelevel.org/cats) 
and [ForEach](https://zio.github.io/zio-prelude/docs/functionalabstractions/parameterizedtypes/foreach) in [zio-prelude](https://zio.github.io/zio-prelude/).

Here is an example with cats:

```scala
import cats.implicits._

List(Some(1), Some(2), Some(3)).sequence
// res: Option[List[Int]] = Some(List(1, 2, 3))

def fetchUser(userId: UserId): IO[User] = ???

List(userId1, userId2).traverse(fetchUser)
// res: IO[List[User]] = ...
```

Here is the same example with zio-prelude:

```scala
import zio.prelude._

List(Some(1), Some(2), Some(3)).flip
// res: Option[List[Int]] = Some(List(1, 2, 3))

def fetchUser(userId: UserId): IO[User] = ???

List(userId1, userId2).forEach(fetchUser)
// res: IO[List[User]] = ...
```