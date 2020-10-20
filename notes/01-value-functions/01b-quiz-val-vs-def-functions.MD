## Quiz

### Which type of function is more friendly to the IDE (Integrated Development Environment)?

1. `val` function
1. (c) `def` function

#### Explanation

`def` functions provide more information to the compiler and IDEs, such as the name of the arguments.

### Which type of function can be stored in a data structure?

1. (c) `val` function
1. `def` function

#### Explanation

`val` functions are ordinary objects in Scala; therefore, we can store them in variables or data structures.

### How do you transform a def function into a val function? e.g. 

```scala   
def greaterThan(x: Int, y: Int): Boolean = x >= y
```

1. (c) `greaterThan _`
1. `greaterThan.toVal`
1. (c) `greaterThan: (Int, Int) => Boolean`

#### Explanation

We can both use an underscore after the method name (eta expansion) or annotate the expected type of the expression 
to give a hint to the Scala compiler.