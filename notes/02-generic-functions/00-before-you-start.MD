## Main objectives
1. Learn how to define classes and functions with type parameters.
1. Understand why we don't pattern match on a generic value.
1. Learn how to leverage type parameters to write fewer and more efficients tests.
1. Recognize when to split arguments of a function (currying) in order to improve type inference.

## New Scala features
1. Transform warning into errors using a compiler option ([video](https://www.fp-tower.com/courses/take/foundations/lessons/14834159-generic-functions-part-2) at 03:00).
1. Scala Worksheets to experiment with an API ([video](https://www.fp-tower.com/courses/take/foundations/lessons/14913605-exercise-1-pair) at 09:41). 
1. Methods used as infix operators ([video](https://www.fp-tower.com/courses/take/foundations/lessons/15034868-exercise-2-predicate) at 01:42).
1. `Try` to catch exceptions ([video](https://www.fp-tower.com/courses/take/foundations/lessons/15034874-exercise-3-jsondecoder) at 05:15).
1. `Gen` and `Arbitrary` to customise random generators in property-based tests ([video](https://www.fp-tower.com/courses/take/foundations/lessons/15034874-exercise-3-jsondecoder) at 11:10).
1. Self-type in traits ([video](https://www.fp-tower.com/courses/take/foundations/lessons/15034874-exercise-3-jsondecoder) at 23:10).

## Syntax

Type parameters must be defined using square brackets (`[]`) after the name of the class, trait or function. 

```scala
case class Pair[A](first: A, second: A)

trait JsonDecoder[A] { }

def size[A](list: List[A]): Int = ???
```

Common type parameter names are single letter variables such as `A`, `B`, `T`, but it is good practice to give them a more meaningful name such as `From`, `To`, `Elem`.

```scala
def map[From, To](list: List[From], update: From => To): List[To] = ???
```

### Multiple parameter lists (currying)

In Scala, methods can have one more list of parameters

```scala
def add1(first: Int, second: Int): Int = 
  first + second

def add2(first: Int)(second: Int): Int = 
  first + second

add1(3, 4) == add2(3)(4)
```
`add1` and `add2` are essentially the same function. 
`add1` is the default syntax, but they are a few situations where `add2` 
is more convenient.


