## What are the two properties of `MonoFoldParam`?

1. (c) `combine` is associative (parentheses don’t matter): `combine(a, combine(b, c)) == combine(combine(a, b), c)`
1. `combine` is commutative (order doesn’t matter): `combine(a, b) == combine(b, a)`
1. (c) `combine` with `default` is a no-operation: `combine(a, default) = a = combine(default, a)`
1. `combine` is idempotent: `combine(a, a) = a`

## Explanation
We only need 1) and 3) to ensure that whichever way we partition the data, `monoFold` on `ParList` always returns the same result as `fold` on List.

Many `combine` operations are commutative, e.g. multiplication `a * b = b * a` or finding the minimum of two numbers `min(a, b) = min(b, a)`.
But, some are not, e.g. String concatenation "Hello" + "World" != "World" + "Hello".

In the bonus exercises, we’ll cover two additional benefits for requiring `combine` to be commutative. I let you think about it.


## Is `sum3` a valid instance of MonoFoldParam?

```scala
val sum3 = new MonoFoldParam[Int] {
  def default: Int = 3

  def combine(first: Int, second: Int) =
    first + second
}
```

1. Yes
1. (c) No 

### Explanation
No, it isn’t because `combine` with `default` is not a no-operation.
For example,

```
combine(default, 2) == combine(3, 2)
                                == 5
                                != 2

```


## Is `multiply` a valid instance of MonoFoldParam?

```scala
val multiply = new MonoFoldParam[Int] {
  def default: Int = 1

  def combine(first: Int, second: Int) =
    first * second
}
```

1. (c) Yes
1. No

### Explanation
Yes, for all integers `x * 1 == x == 1 * x` and `(x * y) * z == x * (y * z)`

## Is `average` a valid instance of MonoFoldParam?

```scala
val average = new MonoFoldParam[Double] {
  def default: Double = 0

  def combine(first: Double, second: Double) =
    (first + second) / 2
}
```

1. Yes
1. (c) No

### Explanation
No, it isn’t because `combine` with `default` is not a no-operation.
```
combine(default, 2) == combine(0, 2)
                                == (0 + 2) / 2
                                == 1
                                != 2
```


Additionally, `average` is not associative, say we take three numbers: 4, 4, and 6.
```
((4 + 4) / 2 + 6) / 2 == 5
(4 + (4 + 6) / 2) / 2 == 4.5
```

## Is `and` a valid instance of MonoFoldParam?

```scala
val and = new MonoFoldParam[Boolean] {
  def default: Boolean = false

  def combine(first: Boolean, second: Boolean) =
    first && second
}
```

1. Yes
1. (c) No

### Explanation
No, it isn’t because `combine` with `default` is not a no-operation.
```
combine(default, true) == combine(false, true)
                                    == false && true
                                    == false
                                    != true
```

If `default` returned `true` then `and` would be a valid instance of `MonoFoldParam`.


### Is `mergeMap` a valid instance of MonoFoldParam?

`mergeMap` is a `MonoFoldParam` of `Map[String, Int]`, which follows two rules:
If a key is in both Maps, then `mergeMap` sums the values (see "Eda" example).
If a key is only in one Map, then `mergeMap` keeps the key-value pair unchanged (see "Bob" and "Jo" examples).

```scala
mergeMap.combine(
  Map("Bob" -> 2, "Eda" -> 1),
  Map("Eda" -> 5, "Jo"  -> 4)
) == Map(
  "Bob" -> 2, // Bob is only in Map 1
  "Eda" -> 6, // Eda is in both Maps, so we get 1 + 5
  "Jo"  -> 4, // Jo is only in Map 2
)

mergeMap.empty == Map.empty
```

1. (c) Yes
1. No

### Explanation
Yes, it is associative because the sum of integers is associative and for all Maps `x`,
```
combine(x, Map.empty) == x == combine(Map.empty, x)
```
Because of rule 2)



