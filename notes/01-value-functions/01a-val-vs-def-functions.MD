Scala 3 simplified the conversion from `Def` to `Val` function (eta expansion). 
For example, in Scala 3 the following code compiles without an underscore.

```scala
def increment(number: Int): Int = number + 1

val incrementVal = increment
// incrementVal: Int => Int = Lambda$1290/0x0000000800ee8040@792e8181
```

You can find more details [here](https://dotty.epfl.ch/docs/reference/changed-features/eta-expansion-spec.html).