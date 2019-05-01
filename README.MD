![FP Foundation Logo](logo/Foundation.png)<br>

[![Join the chat at https://gitter.im/julien-truffaut/fp-foundation](https://badges.gitter.im/julien-truffaut/fp-foundation.svg)](https://gitter.im/julien-truffaut/fp-foundation?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
<span class="badge-patreon"><a href="https://www.patreon.com/bePatron?u=10482033" title="Donate to this project using Patreon"><img src="https://img.shields.io/badge/patreon-donate-yellow.svg" alt="Patreon donate button" /></a></span>

## Synopsis

### [Introduction](https://julien-truffaut.github.io/fp-foundation/index.html#1)

### Chapter 1: [Function](https://julien-truffaut.github.io/fp-foundation/Function#1)
*  Pure functions
*  Polymorphic functions
*  Higher-order functions
*  Recursion
*  Memoization

### Chapter 2: [Type](https://julien-truffaut.github.io/fp-foundation/Type#1)
*   Cardinality
*   Algebra of types
*   Types and Tests, Valid Implementation Count (VIC)
*   Parametricity
*   Logic and types

### Chapter 3: [Typeclass](https://julien-truffaut.github.io/fp-foundation/Typeclass#1)
*   Different techniques to implement ad-hoc polymorphism with Eq
*   Monoid
*   Typeclass laws
*   Instance uniqueness, location
*   Typeclass hierarchy with Semigroup, CommutativeMonoid
*   Use case with distributed computing (e.g. Spark)
*   Higher kinded typeclass with Foldable
*   Typeclass abuses

### Chapter 4: Functors [WIP](https://julien-truffaut.github.io/fp-foundation/Functors#1)
*   Functor, Applicative, Monad
*   Combining effects
*   Typeclass consistency
*   Traverse
*   Typeclass granularity

### Chapter 5: IO / Effects

### Chapter 6: Error Handling

### Chapter 7: Reader / Writer / State


## FAQ

### How to run tests?

```bash
sbt "testOnly $CHAPTER*ExercisesTest"
```

For example for chapter 1

```bash
sbt "testOnly function*ExercisesTest"
```