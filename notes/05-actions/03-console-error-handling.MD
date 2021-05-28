| Type         | GitHub link |
|--------------|-------------|
| Exercises    |     [UserCreationExercises.scala](https://github.com/fp-tower/foundations/blob/master/exercises/src/main/scala/exercises/action/imperative/UserCreationExercises.scala)       |
| Tests        | [UserCreationExercisesTest.scala](https://github.com/fp-tower/foundations/blob/master/exercises/src/test/scala/exercises/action/imperative/UserCreationExercisesTest.scala)   |
| Answers      |       [UserCreationAnswers.scala](https://github.com/fp-tower/foundations/blob/master/answers/src/main/scala/answers/action/imperative/UserCreationAnswers.scala)     |
| Test Answers |   [UserCreationAnswersTest.scala](https://github.com/fp-tower/foundations/blob/master/answers/src/test/scala/answers/action/imperative/UserCreationAnswersTest.scala) |

## Missing code

In the video, I mention a function called `parseDate` which is not included in the code.

```scala
def parseDate(line: String): LocalDate =
  LocalDate.parse(line, dateOfBirthFormatter)
```

We can also define a corresponding formatter function

```scala
def formatDateOfBirth(date: LocalDate): String = 
  dateOfBirthFormatter.format(date)
```