## References
* Example of frameworks using reflection (none recommended):
  * Spring
  * Guice 
* Impact of `List` having a [mutating](https://alexn.org/blog/2021/02/12/scala-list-secret.html) variable. 
* [akka-http](https://doc.akka.io/docs/akka-http/current/routing-dsl/directives/custom-directives.html) API for middleware. 
* [HOCON](https://github.com/lightbend/config/blob/master/HOCON.md) (Human-Optimized Config Object Notation) configuration.
* The [Liskov substitution principle](https://en.wikipedia.org/wiki/Liskov_substitution_principle) is the FP equivalent of "code against the interface"
* `open` [classes](http://dotty.epfl.ch/docs/reference/other-new-features/open-classes.html) in Scala 3.
* `Matchable` [trait](https://dotty.epfl.ch/docs/reference/other-new-features/matchable.html) in Scala 3. 

## How to safely implement Auth middleware

### Approach 1: Sealed Auth

If we add a `sealed` keyword to the `Auth` interface, this will force us to define all `Auth` implementations in the same file. This means we will know all implementations of the trait at compile time, and as a result, we can use exhaustive pattern matching.

This approach is only applicable if we control the `Auth` interface; for example, if it is within our own codebase and we don’t want other people to extend it. 

### Approach 2: Expose a flag
We can add an extra method to the `Auth` public interface to inform users that it is meant for testing purposes. For example:
```scala
trait Auth {
   // returns true if this instance is meant for testing purposes
   def isMock: Boolean
   
   def authenticate(token: Token): Future[User]
}

def checkUser(request: Request, auth: Auth, env: Env) =
 (env, auth.isMock) match {
   case (Prod, true) => InternalServerError()
   case _                 => // check header and dispatch to auth
 }
```
Since `isMock` is part of the `Auth` contract, it is safe to trust it when building application logic. 
It is the responsibility of all `Auth` implementations (including `CachedAuth`) to determine whether they are a test implementation or not.

However, this approach is disappointing because it exposes implementation details in the `Auth` public contract, which is an anti-pattern.

### Approach 3: Parse and validate the config
The core of the problem is that it is not the responsibility of `Auth` middleware to check if we are using a `MockAuth` in production. 
I would argue the best place to write this validation logic is where we build the `Auth` instance from the configuration file. Being an open class, `Auth` has an unbounded number of implementations; however, our application can only accept a few options because we build the instance by parsing a configuration file. 
```scala
def createAuth(config: Config): Auth = 
  config.getString(“type”) match {
    case “Okta” => …
    case “Mock” => …
    case “Cached” => …
    case other => throw new IllegalArgumentException(s“Unsupported Auth type $other”)
  }
```
Instead, we could extend the `createAuth` function with an environment variable (`Local`, `UAT` or `Prod`). This way, if we build a `MockAuth` instance in `Prod`, we can either throw an exception or log a message.
```scala
def createAuth(config: Config, env: Env): Auth = 
  config.getString(“type”) match {
    case “Okta” => …
    case “Mock” => 
      env match {
        case Prod => throw new IllegalArgumentException(s“Cannot use MockAurth in Prod”)
        case Local | UAT => … 
      }
    case “Cached” =>
      val underlying = createAuth(config.getObject(“underlying”), env) // recursive call
      …
    case other => throw new IllegalArgumentException(s“Unsupported Auth type $other”)
  }
```
Note that in the `Mock` case, we pattern match exhaustively on the environment. This ensures we cover all cases. We will get a warning if a new environment is added later on. And in the `Cached` case, we recursively call `createAuth`, so if the underlying `Auth` instance is a `Mock`, it will throw an exception.

You might note that `createAuth` is not an FP function, but it is acceptable in this case. 
We are going to discuss it in more detail in the next chapter. The core idea is that it is ok to perform actions at the initialisation of the application or more generally in the “Main” file. What matters is that we don’t throw exceptions in the core logic of our application.

## Examples of classes meant for extension

### Functionality

```scala
trait Ordering[A] {
  //The result sign has the following meaning:
  //  - negative if first < second
  //  - positive if first > second
  //  - zero otherwise (if first == second)
  def compare(first: A, second: A): Int
}
```
```scala
trait Logger {
  def debug(message: String): Unit
  def error(message: String, error: Throwable): Unit
}
```

### Data structures

A `Map` is a data structure that looks up data by keys, but there are no guarantees of how the values are ordered.

```scala
val users1: Map[Int, User] = SortedMap(
  5555 -> User(5555, "Alice"),
  1234 -> User(1234, "Bob"  ),
)

val users2: Map[Int, User] = ListMap(
  5555 -> User(5555, "Alice"),
  1234 -> User(1234, "Bob"  ),
)

users1.get(1234) == users2.get(1234)
// res: Boolean = true

users1.head == users2.head
// res: Boolean = false

users1.head._2
// res: User = User(1234, "Bob")

users2.head._2
// res: User = User(5555, "Alice")
```

### Business logic

```scala
trait OrderApi {
  def get   (orderId: OrderId): Future[Order]
  def submit(order  : Order  ): Future[Unit]
  def cancel(orderId: OrderId): Future[Unit]
}
```

Finally, `Any`, `AnyVal` and `AnyRef` are all open traits.
