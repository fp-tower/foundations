| Type         | GitHub link |
|--------------|-------------|
| Exercises    |     [UserCreationExercises.scala](https://github.com/fp-tower/foundations/blob/master/exercises/src/main/scala/exercises/action/imperative/UserCreationExercises.scala)       |
| Tests        | [UserCreationExercisesTest.scala](https://github.com/fp-tower/foundations/blob/master/exercises/src/test/scala/exercises/action/imperative/UserCreationExercisesTest.scala)   |
| Answers      |       [UserCreationAnswers.scala](https://github.com/fp-tower/foundations/blob/master/answers/src/main/scala/answers/action/imperative/UserCreationAnswers.scala)     |
| Test Answers |   [UserCreationAnswersTest.scala](https://github.com/fp-tower/foundations/blob/master/answers/src/test/scala/answers/action/imperative/UserCreationAnswersTest.scala) |

## Custom random generators

`arbitrary` lets us fetch the default random generator for a type, if there is one:

```scala
import org.scalacheck.Arbitrary.arbitrary

arbitrary[String]
// res: Gen[String] = ...
```

Then, we can use the methods `filter` or `filterNot` to modify an existing generator.

```scala
val invalidYesNoInputGen: Gen[String] = 
  arbitrary[String]
    .filter(word => word != "Y" && word != "N")
```

`invalidYesNoInputGen` is a random generator of `String` which will never 
produce the single letter String "Y" and "N".

Note that if the predicate of `filter` is too restrictive, it 
could take a long time for finding a valid value. For example, this generates 
random Strings and discards all values that do NOT start with "abc".

```scala
arbitrary[String].filter(_.startsWith("abc"))
```

To prevent this situation, Scalacheck throws an Exception if too many values are discarded.

```bash
Gave up after 0 successful property evaluations. 51 evaluations were discarded.
```

If you wanted a generator of String starting with "abc", a better solution would be to generate a 
random String and then, prepend it with "abc".

```scala
arbitrary[String].map(randomString => "abc" ++ randomString)
```