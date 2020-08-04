
## Val function

Example:
```scala
val isPositive: Int => Boolean = 
  (number: Int) => number >= 0 
```

* A `Val` function (a.k.a. lambda, anonymous function, function literal) is an ordinary object. Hence, it can be:
   * Stored in a value or in a data structure e.g. `List`, `Map`.
   * Used as input of another function e.g. `filter`, `map`.
   * Used as output of another function.
* Under the hood a `Val` function is an instance of `FunctionX` where `X` is a number between 0 and 22 representing the 
number of arguments to the function.
* `FunctionX` is a trait with only one abstract method, also known as Single Abstract Method type (SAM type). Scala and Java
have a very concise syntax to create instances of SAM type.

## Def function

Example:
```scala
def isPositive(number: Int): Boolean = 
  number >= 0 
```

* A `Def` function (a.k.a. method) is the standard way to create a function in Scala.
* Argument names are part of the signature which offers 
   * better error message 
   * possibility to name the arguments, e.g. `login(username = "bob", password = "Welcome1")`
* Better stack trace (TODO example)
* A `Def` function can easily be converted to a `Val` function
   * In most cases, it is done automatically by the compiler
   * When it doesn't work, we can use eta expansion by adding an `_` after the method's name, e.g. `login _`     

## Property-Based Testing (PBT)

Example:
```scala
test("filter reduces the size of a List") {
  forAll{ (numbers: List[Int], predicate: Int => Boolean) =>
    assert(
      numbers.filter(predicate).size <= numbers.size
    )
  }
}
```