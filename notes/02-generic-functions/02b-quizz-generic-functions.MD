# Quiz: Generic functions

### Which method signature is valid in Scala 2?

1. `def doSomething(value: A): Int`
1. (c) `def doSomething[A](value: A): Int`
1. `def<A> doSomething(value: A): Int`
1. `def[A] doSomething(value: A): Int`
1. `val doSomething: [A] => A => Int`

#### Explanation

In Scala, we define type parameters within square brackets after the method's name. 
We do the same for generic classes and traits:

```scala
case class Id[A](id: Long, value: A)

trait ExcelParser[A] {
  def parse(workbook: Workbook): A
}
```

In Scala 2, we can only define generic `def` functions. It is impossible to define a generic `val` function. 
Scala 3 removed this limitation by introducing polymorphic function types:

```scala
val doSomething: [A] => A => Int
```

### What are the benefits of generic functions?

1. (c) Reduce code duplication
1. (c) Better documentation
1. Faster execution
1. (c) Fewer tests

#### Explanation

1. Reduce code duplication: Generic functions are applicable to many types. This means we don't need to 
copy/paste the same code to use it on different types. For example, `size(List(1, 2))` or `size(List("Hello", "World"))` 
1. Better documentation: When a function accepts generic inputs, it documents that these values are treated as black boxes
by the implementation. This is very helpful to understand what a function can or cannot do without looking at the code. 
1. Fewer tests: Generic functions must treat all types in the same way. This means if we check that a function works correctly
for one type, we know it will do exactly the same for all other types.


### Which of the following methods provide the best type inference?

```scala
def zipV1[A, B, To](list1: List[A], list2: List[B], combine: (A, B) => To): List[To]

def zipV2[A, B, To](list1: List[A], list2: List[B])(combine: (A, B) => To): List[To]
```

1. `zipV1`
1. (c) `zipV2`

#### Explanation

In Scala 2, type inference works one set of parentheses at a time. So for `zipV1`, the compiler cannot use
type information from `list1` and `list2` arguments to infer the input type of the `combine` function. For example:

```scala
zipV1(List(1, 2), List(0, 0), _ + _) // fails to compile

zipV2(List(1, 2), List(0, 0))(_ + _) // works fine.
```

It is recommended to use a different set of parentheses (currying) when a generic method takes a `val` function using
generic types.  