## Main objectives
1. Learn how to define classes and functions with type parameters for better code reusability.
1. Understand why we don’t check the value of a type parameter at runtime (pattern matching).
1. Learn how to leverage generic rules to write fewers and more efficients tests.
1. Recognize when to split arguments of a function (currying) to improve type inference.

## New Scala features
1. Scala Worksheets to experiment with an API.
1. Methods used as infix operators
1. Self-type in traits.
1. `Try` to catch exceptions.
1. `Gen` and `Arbitrary` to generate random generators for property-based-testing.

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


