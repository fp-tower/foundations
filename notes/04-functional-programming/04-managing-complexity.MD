## References
* [Plain Functional Programming](https://www.youtube.com/watch?v=YXDm3WHZT5g) by Martin Odersky
* Golden tests, example [circe-golden](https://github.com/circe/circe-golden) for checking JSON documents.
* Immutable binary data:
   * [Scodec](http://scodec.org/) 
   * [Akka](https://doc.akka.io/api/akka/current/akka/util/ByteString.html)
* [Single Responsibility Principle](https://en.wikipedia.org/wiki/Single-responsibility_principle)

## Functional programming and Scala are complex

FP and Scala don’t have a reputation for being simple. In my opinion, this is because they let us express extremely abstract concepts. As a result, we have a wide range of options available and it is difficult to choose the right level of abstraction for the task at hand. For example, addition, as in summing up numbers, is one of the simplest concepts in maths. Yet, addition can become quite complex when we generalise it to work on other types such as `String` or `Map`, as we did with `Monoids` in chapter 3. 
Now, the benefit of `Monoids` is that we can run a large number of operations in parallel. This allows us to have a simpler and more extensible parallel algorithm, but it means that every developer has to learn how to work with Monoids. In this case, I think the benefits outweigh the costs.
However, it would be overkill to introduce Monoid solely to calculate the total amount of the invoice.

So, just because we can use high-level abstractions, it doesn’t mean that we should. It is good to have the option to utilize these powerful abstractions when necessary but in most cases, the simpler the abstraction, the better. That’s why this course concentrates on relatively straightforward FP techniques, as they offer most of the benefits without a steep learning curve. 