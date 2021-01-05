## Quiz: FP functions

### Is `isWeekend` a function according to functional programming?

```scala
sealed trait Day
case object Monday  extends Day
// ...
case object Saturday extends Day
case object Sunday   extends Day

def isWeekend(day: Day): Boolean = 
  day match {
    case Saturday | Sunday => true
    case _                 => false
  }
```

1. (c) true
1. false

#### Explanation

`isWeekend` could be replaced by the following Map:

```scala
Map(
  Monday -> false, Tuesday -> false, Wednesday -> false, Thursday -> false, Friday -> false,
  Saturday -> true, Sunday -> true
)
``` 

### Is `findEditor` a function according to functional programming?

```scala
sealed trait Role

case class Reader(accountId: Long, premiumUser: Boolean) extends Role
case class Editor(accountId: Long, favoriteFont: String) extends Role

def findEditor(accountId: Long, roles: List[Role]): Option[Editor] =
  roles match {
    case Nil => None
    case (_: Reader) :: otherRoles => 
      findEditor(accountId, otherRoles)
    case (editor: Editor) :: otherRoles if editor.accountId == accountId => 
      Some(editor)
  }
```

1. true
1. (c) false

#### Explanation

`findEditor` is a partial function. We didn't cater for the case where the first element of the list
is an `Editor` but the `accountId` does not match. For example, the following code would throw an Exception: 

```scala
findEditor(0, List(Editor(accountId = 1, favoriteFont = "Comic Sans")))
``` 

### Is `createBlogEntry` a function according to functional programming?

```scala
import java.time.LocalDate
import java.util.UUID

case class BlogEntry(id: UUID, title: String, publishedAt: LocalDate)

def createBlogEntry(title: String): BlogEntry =
  BlogEntry(
    id          = UUID.randomUUID(),
    title       = title,
    publishedAt = LocalDate.now()
  )
```

1. true
1. (c) false

#### Explanation

`createBlogEntry` is not deterministic. If we call the function several times with the same title 
we will get different results because of `UUID.randomUUID()` and `LocalDate.now()`.

### Is `max` a function according to functional programming?

```scala
def max[A](list: List[Int]): Option[Int] = {
  var state = Option.empty[Int]
  for (element <- list)
    state match {
      case None             => state = Some(element)
      case Some(currentMax) => if (currentMax < element) state = Some(element)
    }
  state
}
```

1. (c) true
1. false

#### Explanation

Even though `max` uses a mutating variable, it is an FP function because the mutation
is not visible outside of the body of the function. In other words, it is an 
implementation details.

### Are `execFizzBuzz` and `fizzBuzz` functions according to functional programming?

```scala
def execFizzBuzz: Unit =
  (1 until 100)
   .map(fizzBuzz)
   .map(println)

def fizzBuzz(x: Int): String = 
  if      (x % 15 == 0) "FizzBuzz"
  else if (x %  3 == 0) "Fizz"
  else if (x %  5 == 0) "Buzz"
  else x.toString
```

1. true for both
1. true for `execFizzBuzz` but false for `fizzBuzz`
1. (c) false for `execFizzBuzz` but true for `fizzBuzz`
1. false for both

#### Explanation

`fizzBuzz` is an FP function, it could be replaced by the following Map:
```scala
Map(
// ...
 -1 -> "-1", 
  0 -> "FizzBuzz",
  1 -> "1",
  2 -> "2",
  3 -> "Fizz",
  4 -> "4",
  5 -> "Buzz",
  6 -> "Fizz",
//  ...
)
``` 

However, `execFizzBuzz` prints the result of `fizzBuzz` to the command line which is a side effect. 

###  Are `getOrder` and `insertOrder` functions according to functional programming?

```scala
case class Order(clientId: Long, orderId: Long, product: String, price: Double)
private var orders: Map[Long, Order] = Map.empty

def getOrder(clientId: Long, orderId: Long): Option[Order] =
  orders
   .get(orderId)
   .filter(_.clientId == clientId)

def insertOrder(order: Order): Unit =
  orders = orders.updated(order.orderId, order)
```

1. true for both
1. true for `getOrder` but false for `insertOrder`
1. false for `getOrder` but true for `insertOrder`
1. (c) false for both

#### Explanation

`getOrder` is non-deterministic, for example

```scala
getOrder(0, 0) 
// res: Option[Order] = None

insertOrder(Order(0, 0, "banana", 1.2))

getOrder(0, 0) 
// res: Option[Order] = Some(Order(0, 0, "banana", 1.2))
```

`insertOrder` returns `Unit` which is a sign this function performs a side effect.

### Is `addUser` a function according to functional programming?

```scala
import scala.concurrent.Future

case class User(id: Long, name: String)

class UserRepository(db: Db) { // assume some database library
  def addUser(user: User): Future[Unit] = 
    db.run("INSERT INTO users (id, name) VALUES (${user.id}, ${user.name})") 
}
```

1. true
1. (c) false

#### Explanation

`addUser` does a side effect when it contacts the database to insert a user. 

### Is `circleArea` a function according to functional programming?

```scala
val pi = 3.14
def circleArea(radius: Double): Double =
  radius * radius * pi
```

1. (c) true
1. false

#### Explanation

`circleArea` references an external value, but it is a constant (immutable val). 
Therefore, we could inline `pi` within the body of `circleArea` without changing the results.

### Is `increment` a function according to functional programming?

```scala
def increment(array: Array[Int]): Unit =
  for { i <- array.indices } 
    array(i) = array(i) + 1
```

1. true
1. (c) false

#### Explanation

`increment` updates a mutable array. 
`increment` returns `Unit` which is a sign this function performs a side effect.

### Is `Item.apply` a function according to functional programming?

```scala
case class Item(id: Long, name: String, quantity: Int, price: Double) {
  require(quantity > 0, "quantity must be positive")
  require(price > 0, "price must be positive")
}
```

1. true
1. (c) false

#### Explanation

The constructor of `Item` will throw an Exception if quantity or price is inferior or equal to 0.
For example,
```scala
Item(1, "bottle", -2, 3.29)
// java.lang.IllegalArgumentException: requirement failed: 
//   quantity must be positive
```

### Is `isOverdue` a function according to functional programming?

```scala
import java.time.LocalDate

case class Invoice(id: Long, amount: Double, dueDate: LocalDate) {
  def isOverdue: Boolean =
    LocalDate.now().isAfter(dueDate)
}
```

1. true
1. (c) false

#### Explanation

`isOverdue` is not deterministic. It is result will change over time, for example, it may return 
`false` right now because `LocalDate.now()` is before `dueDate` but if we wait long enough,
`isOverdue` will return `true` for the same invoice.

One way to make this function deterministic is to pass the current date as an input:
```scala
import java.time.LocalDate

case class Invoice(id: Long, amount: Double, dueDate: LocalDate) {
  def isOverdue(today: LocalDate): Boolean =
    today.isAfter(dueDate)
}
```

Of course, we still need to use `LocalDate.now()` at some point, which is non-deterministic, but the 
higher we do it in the call stack, the better.

## Is `getAge` a function according to functional programming?

```scala
class User(initialAge: Int) {
  private var age: Int = initialAge

  def getAge: Int = 
    age

  def setAge(newAge: Int): Unit =
    age = newAge
}
```

1. true
1. (c) false

#### Explanation

`getAge` is not deterministic. `setAge` changes the result of `getAge`, as you can see below:
```scala
val john = new User(24)

john.getAge
// res: Int = 24

john.setAge(32)

john.getAge
// res: Int = 32
```

This is the case for all API based on publicly available mutating variable.