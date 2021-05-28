# Notes

* Documentation for the [Infix operator syntax](https://docs.scala-lang.org/tour/operators.html)
* If you couldn't implement `contramap` or `by` functions, don't worry. It is completely normal. These questions were meant to be difficult. Try again a week or two and don't hesitate to ask questions on Slack!

| Type         | GitHub link |
|--------------|-------------|
| Exercises    | [GenericFunctionExercises.scala](https://github.com/fp-tower/foundations/blob/master/exercises/src/main/scala/exercises/generic/GenericFunctionExercises.scala) |
| Tests        | [GenericFunctionExercisesTest.scala](https://github.com/fp-tower/foundations/blob/master/exercises/src/test/scala/exercises/generic/GenericFunctionExercisesTest.scala) |
| Answers      | [GenericFunctionAnswers.scala](https://github.com/fp-tower/foundations/blob/master/answers/src/main/scala/answers/generic/GenericFunctionAnswers.scala) |
| Test Answers | [GenericFunctionAnswersTest.scala](https://github.com/fp-tower/foundations/blob/master/answers/src/test/scala/answers/generic/GenericFunctionAnswersTest.scala) |

## How to improve type inference of by

As we saw in the video when a function or a data type has several type parameters,
we cannot partially define them, for example:

```scala
case class Data[A, B](first: A, second: B)

def fun[A, B](first: A, second: B) = ???

fun(4, "String") // We let Scala infer A and B
fun[Int, String](4, "String") // We manually specify the value of A and B

fun[Int](4, "String")
// [error] wrong number of type parameters for method fun
```

This is annoying because for `by`, we would like to only specify the first 
type parameter and let the Scala compiler infer the second type, e.g.

```scala
def by[From, To](zoom: From => To)(predicate: Predicate[To]): Predicate[From] = ???

by[User](_.age)(isBiggerThan(18))
// [error] wrong number of type parameters for method by
```

There are two possible solutions. First, we could annotate the input of the zoom function

```scala
by((user: User) => user.age)(isBiggerThan(18))
// which can be shorten to
by((_: User).age)(isBiggerThan(18))
```

This is not really satisfactory because it requires the caller of `by` to know this trick.

The problem is that type parameters cannot be curried like function parameters are:
```scala
def by[From][To] // doesn't work
```

There is a workaround, we can create a dummy class with a single `apply` method:

```scala
class ByPartiallyApplied[From] {
  def apply[To](zoom: From => To)(predicate: Predicate[To]): Predicate[From] = ???
}
```

And then change `by` so that it creates an instance of `ByPartiallyApplied`:

```scala
def by[From]: ByPartiallyApplied[From] = new ByPartiallyApplied[From] {}

by[User].apply(_.age)(isBiggerThan(18))
// which can be shorten to
by[User](_.age)(isBiggerThan(18))
```

I would have never found this trick myself, and I am pretty sure Scala wasn't design with this in mind, but it works.