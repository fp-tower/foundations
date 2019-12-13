![FP Foundation Logo](logo/Foundation.png)<br>

[![Join the chat at https://gitter.im/fp-tower/foundation](https://badges.gitter.im/julien-truffaut/fp-foundation.svg)][gitter]
<span class="badge-patreon"><a href="https://www.patreon.com/bePatron?u=10482033" title="Donate to this project using Patreon"><img src="https://img.shields.io/badge/patreon-donate-yellow.svg" alt="Patreon donate button" /></a></span>

## Target audience

* New Scala developers 
* Experienced Scala developers who mostly used the object-oriented side of Scala

We had both successes with people utterly new to Scala and Scala developers with more 
than eight years of experience. The course will be useful to you except if you are already 
an expert in functional programming.

## Requirements
Some familiarity with Scala syntax. For example, a couple of month of professional experience 
in Scala or attending a Scala [MOOC](https://www.coursera.org/learn/progfun1)

## Plan

### Theme 1: Essential of functional programming

We look at what is functional programming, what are the benefits and restrictions of using this 
paradigm in Scala. The goal is to be able to write an entire program following functional programming 
principles.

| Module 1: [Function](https://fp-tower.github.io/foundation/1-Function.html#1)  | Module 2: [Side Effect](https://fp-tower.github.io/foundation/2-SideEffect.html#1) |
| :--------------------- | ----------------------- |
| First class functions  | How to control effects  |
| Polymorphic functions  | Thunk / IO              |
| Recursion, Lazyness    | IO vs Future            |
| Pure functions         | Async IO                |
| Memoization            | Restricted algebra      |

### Module 1: [Function](https://fp-tower.github.io/foundation/1-Function.html#1)
* First class functions
* Polymorphic functions
* Recursion, Lazyness
* Pure functions
* Memoization

### Module 2: [Side Effect](https://fp-tower.github.io/foundation/2-SideEffect.html#1)
* How to control effects
* Thunk / IO
* IO vs Future
* Async IO
* Restricted algebra

### Module 3: [Error Handling](https://fp-tower.github.io/foundation/3-ErrorHandling.html#1)
* How to safely deal with runtime errors
* Compare error types: Option, Try, Either
* Covariance
* Introduction to OptionT, EitherT

### Module 4: [Type](https://fp-tower.github.io/foundation/4-Type.html#1)
* Unrepresentable state
* Cardinality
* How to measure the impact of types and tests
* Parametricity
* Relationship between logic, algebra and types


## FAQ

### Where should I start?

Each module has:
* a set of slides (linked above) located at `docs/$module.html`
* a scala object containing all exercises located at `exercises/src/main/scala/exercises/$module/$moduleExercises.scala`
* a scala object containing all answers located at `exercises/src/main/scala/answers/$module/$moduleAnswers.scala`
* two test suites (one for exercises, one for answers) located at `exercises/src/test/scala/exercises/$module/$moduleTest.scala`

Start each module with the slides, at some point you will be prompted to work on some exercises. 
Do the exercises and check your answers are correct by running the tests for this module.
Finally, go back to the slides and repeat the process. If you have any question, please ask them on [gitter][gitter].

### How to run the tests?

```bash
sbt testExercises$X
```

For example tests for module 3 (Type) is run with:

```bash
sbt testExercises3
```

### Is this course free?

Yes, the material is open source and it uses a permissive licence [CC-BY-SA-4.0][licence].
However, the slides and exercises are currently made to be run by a trainer. Eventually, we aim to make this course
self-study with online videos but it is not likely to happen before mid 2020.

We do encourage people who enjoy the course to make a donation using [patreon][patreon].

[gitter]: https://gitter.im/fp-tower/foundation?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge
[licence]: https://creativecommons.org/licenses/by-sa/4.0/legalcode
[patreon]: https://www.patreon.com/bePatron?u=10482033