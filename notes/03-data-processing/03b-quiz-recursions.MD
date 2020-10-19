## Quiz recursions

### Which of these datastructures is recursive?

```scala
case class Person(name: String, age: Int, email: Option[String])

case class Order(quantity: Int, price: Double, previousOrder: Option[Order])

sealed trait MovingStorage
case class Box(width: Int, height: Int) extends MovingStorage
case class Truck(foot: Int) extends MovingStorage
```

1. Person
1. (c) Order
1. MovingStorage
1. Option
1. (c) Json


### What does this code do?

```scala
import java.time.{Duration, Instant}

def loop(until: Instant, sleepDuration: Duration): Unit = {
  val now = Instant.now()

  if (now.isAfter(until)) ()
  else {
    println(s"Time is $now")
    Thread.sleep(sleepDuration.toMillis)
    loop(until, sleepDuration)
  }
}

loop(
  until         = Instant.now().plus(Duration.ofHours(2)),
  sleepDuration = Duration.ofSeconds(5)
)
```

1. Print the time every 2 seconds for 5 hours
1. Do nothing
1. (c) Print the time every 5 seconds for 2 hours
1. Print the time every 5 seconds forever

#### Explanation

`loop` prints the current time (`now`) as long as the deadline (`until`) hasn't been reached.
So after 2 hours (`Instant.now().plus(Duration.ofHours(2))`) the code will stop.

Between each `println`, the code calls `Thread.sleep(x)` which pauses the execution for `x` milliseconds,
in our case 5 seconds (`sleepDuration`).


### What are the main things to be careful of with recursions?

1. (c) Termination conditions
1. NullPointerException
1. (c) StackOverflowError
1. Mutations

#### Explanation

The most common mistake of recursions is to have invalid termination conditions, for example,
if we check that `n == 0` instead of `n <= 0`. This may cause the recursion to run forever or
to terminate earlier/later than expected. It is good practice using a unit test to verify a
recursion terminates correctly.

If a function is not tail-recursive, then it will throw a `StackOverflowError`
if it calls itself too many times. We can ensure this doesn't happen by limiting the number of
recursive calls (e.g. to a few hundreds) or by making the function tail-recursive (not always possible).