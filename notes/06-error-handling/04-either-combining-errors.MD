| Type         | GitHub link |
|--------------|-------------|
| Exercises    |     [EitherExercises2.scala](https://github.com/fp-tower/foundations/blob/master/exercises/src/main/scala/exercises/errorhandling/either/EitherExercises2.scala)       |
| Tests        | [EitherExercises2Test.scala](https://github.com/fp-tower/foundations/blob/master/exercises/src/test/scala/exercises/errorhandling/either/EitherExercises2Test.scala)   |
| Answers      |       [EitherAnswers2.scala](https://github.com/fp-tower/foundations/blob/master/answers/src/main/scala/answers/errorhandling/either/EitherAnswers2.scala)     |
| Test Answers |   [EitherAnswers2Test.scala](https://github.com/fp-tower/foundations/blob/master/answers/src/test/scala/answers/errorhandling/either/EitherAnswers2Test.scala) |

## Sealed trait vs sealed abstract class

In Scala 2, traits cannot have constructors. For example, this code doesn't compile

```scala
trait Country(countryCode: String) {}
```

So, if we want to create an enumeration with a constructor, we need to use a `sealed abstract class`

```scala
sealed abstract class Country(countryCode: String) {}
```

Scala 3 have lifted this limitation which means there is almost no use case for `abstract class` in Scala 3.

## Enumeratum

[Enumeraturm](https://github.com/lloydmeta/enumeratum) is a library with 0-dependency that 
adds lots of utility methods to enumerations (`sealed trait` or `sealed class`). For example,
we could have used `findValues` to compute all the values of type `Country`.

```scala
sealed trait Country extends EnumEntry

object Country extends Enum[Country] {
  val all = findValues // computes the exhaustive list of countries

  case object France        extends Country("FRA")
  case object Germany       extends Country("DEU")
  case object Switzerland   extends Country("CHE")
  case object UnitedKingdom extends Country("GBR")
}
```