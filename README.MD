![FP Foundation Logo](logo/Foundation.png)<br>

[![Join the chat at https://gitter.im/julien-truffaut/fp-foundation](https://badges.gitter.im/julien-truffaut/fp-foundation.svg)][gitter]
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

### Where should I start?

Each chapter has:
* a set of slides (linked above) located at `docs/$chapter.html`
* a scala object containing all exercises located at `exercises/src/main/scala/exercises/$chapter/$chapterExercises.scala`
* a scala object containing all answers located at `exercises/src/main/scala/answers/$chapter/$chapterAnswers.scala`
* two test suites (one for exercises, one for answers) located at `exercises/src/test/scala/exercises/$chapter/$chapterTest.scala`

Start each chapter with the slides, at some point you will be prompted to work on some exercises. 
Do the exercises and check your answers are correct by running the tests for this chapter.
Finally, go back to the slides and repeat the process. If you have any question, please ask them on [gitter][gitter] 

### How to run the tests?

```bash
sbt "testOnly $CHAPTER*ExercisesTest"
```

For example for chapter 1

```bash
sbt "testOnly function*ExercisesTest"
```

### Is this course free?

Yes, the material is open source and it uses a permissive licence [CC-BY-SA-4.0][licence].
However, the slides and exercises are currently made to be run by a trainer. Eventually, we aim to make this course
self-study with online videos but it is not likely to happen before mid 2020.

[gitter]: https://gitter.im/julien-truffaut/fp-foundation?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge
[licence]: https://creativecommons.org/licenses/by-sa/4.0/legalcode