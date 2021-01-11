## Refactoring: extract - inline

```scala
def helloV1(user: User) = {
  funA(user) && funB(user)
}
```

```scala
def helloV2(user: User) = {
  val res1 = funA(user)
  val res2 = funB(user)
  res1 && res2
}
```


```scala
def funA(user: User): Boolean =
  user.age > 18

def funB(user: User): Boolean =
  if(user.country == "UK")
    throw NotSupportedCountry
  else
    user.balance >= 0
```

```scala
val bob = User(
  name = "Bob",
  age = 15,
  country = "UK",
  balance = 500
)

helloV1(bob)
// `res: Boolean = false`

helloV2(bob)
// `error: NotSupportedCountry`
```