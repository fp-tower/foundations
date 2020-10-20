## Quiz for-loop

### With which types can we use a for-loop?

1. (c) List
1. (c) Map
1. Int
1. (c) String
1. Tuple2, for example (5, "Hello")

#### Explanation

A for-loop (without `yield`) is an alternate syntax for the method `foreach`. 
This method is available on all collections (`List`, `Array`, `Map`) and on `String`, 
which is viewed as a collection of `Char` in Scala.

`Int` and `Tuple2` do not have a foreach method.

### What is the 4th printed line?

```scala
for {
  width  <- List(1, 2, 3)
  height <- List(4, 5)
} println("area is ${width * height}")
```

1. "area is 12"
1. Nothing, this code prints only 3 lines 
1. "area is 5"
1. (c) "area is ${width * height}"

#### Explanation

The `s` String interpolator is missing which means `${width * height}` is not interpreted.

If we add the String interpolator, then "area is 10" would be printed because the for-loop will evaluate the variables in the following order:
1. width = 1, height = 4 
1. width = 1, height = 5 
1. width = 2, height = 4
1. width = 2, height = 5 
1. width = 3, height = 4 
1. width = 3, height = 5

### What is printed?

```
case class User(name: String, email: Option[String])
val users = List(
  User("John", Some("john@foo.com")),
  User("Eda ", None),
  User("Kim" , Some("kim@bar.com")),
)

for {
  user  <- users
  email <- user.email
} println(email)
```

1. Print "john@foo.com" and stops
1. Print "john@foo.com", "", and then "kim@bar.com"
1. (c) Print "john@foo.com" and then "kim@bar.com"
1. Does not compile

#### Explanation

This for-loop is equivalent to:

```scala
users.foreach { user =>
  user.email.foreach { email =>
    println(email) 
  }
}
```

The first `foreach` iterates over all elements in the List.
The second `foreach` is only executed if `email` is a `Some`. 
You can think of an `Option` as a `List` with at most one element.

### What does the following code produce?

```scala
List('a', 'b', 'c', 'd')
  .foldLeft("")((state, char) => char + state)
```

1. "abcd"
1. ""
1. (c) "dcba"
1. Does not compile

#### Explanation

Each character of the input list is prepended to `state`. So if we iterate step by step we get:
1. state: "", char: 'a'
1. state: "a", char: 'b'
1. state: "ba", char: 'c'
1. state: "cba", char: 'd'
1. state: "dcba"

### What does the following code produce?

```scala
List(1, 2, 3, 4).foldLeft(0)(_ * _)
```

1. 24
1. 10
1. (c) 0
1. Does not compile

#### Explanation

This code is equivalent to:

```scala
var state = 0

for (number <- List(1, 2, 3, 4))
  state = state * number

state
```

So on each iteration `state` is set to `0` because `0 * 1 = 0`, `0 * 2 = 0`, and so on.

If we change the default value from `0` to `1`, then we get:

```scala
List(1, 2, 3, 4).foldLeft(1)(_ * _) == 24
```
