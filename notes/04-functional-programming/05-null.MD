## References

* The Scala functional subset is also known as the Scalaz subset after the name of one of the first FP libraries, [Scalaz](https://github.com/scalaz/scalaz). 
Now, [cats](https://typelevel.org/cats/) and [ZIO](https://github.com/zio/zio-prelude) are more popular alternatives to scalaz.
* [Tony Hoare](https://en.wikipedia.org/wiki/Tony_Hoare#Apologies_and_retractions), the inventor of `null`, has famously called it his billion-dollar mistake.
* A `sealed abstract class` is the same thing as a `sealed trait`. It is an enumeration. 
However, in Scala 2, traits don’t have parameters (e.g. `val rate` for `VAT`), so we had to use an abstract class. In Scala 3, this limitation has been [removed](http://dotty.epfl.ch/docs/reference/other-new-features/trait-parameters.html).
This means we could encode `VAT` as a `sealed trait` or even as an `enum` in Scala 3.
* `Option` has a `get` method, which allows you to access the element inside it. However, it is not recommended to use it because it throws an `Exception` if the `Option` is empty.
* Scala 3 [opaque types](http://dotty.epfl.ch/docs/reference/other-new-features/opaques.html) may reduce/erase the runtime cost of `Option`.
* [Explicit](https://dotty.epfl.ch/docs/internals/explicit-nulls.html) `nulls` in Scala 3. In particular, take a look at the section "Java interop" to see how it will affect Java code used in Scala.
* Scala 3 [union types](http://dotty.epfl.ch/docs/reference/new-types/union-types.html)
