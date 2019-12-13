![FP Foundation Logo](logo/Foundation.png)<br>

[![Join the chat at https://gitter.im/fp-tower/foundation](https://badges.gitter.im/julien-truffaut/fp-foundation.svg)][gitter]
<span class="badge-patreon"><a href="https://www.patreon.com/bePatron?u=10482033" title="Donate to this project using Patreon"><img src="https://img.shields.io/badge/patreon-donate-yellow.svg" alt="Patreon donate button" /></a></span>

## Target audience

1. New Scala developers 
2. Experienced Scala developers who mostly used the object-oriented side of Scala

We had both successes with people utterly new to Scala and Scala developers with more 
than eight years of experience. The course will be useful to you except if you are already 
an expert in functional programming.

## Requirements

Some familiarity with Scala syntax. For example, a couple of month of professional experience 
in Scala or attending a Scala [MOOC](https://www.coursera.org/learn/progfun1)

## Course content

### Part 1: Essential of functional programming

In this section, we look at what is functional programming, what are the benefits and restrictions of 
using this paradigm in Scala. 

### Module 1: [Function](https://fp-tower.github.io/foundation/1-Function.html#1)

Scala is a language with first-class functions, this simple fact has a tremendous impact on how we can program.
In this module, we analyse the various syntax and concepts around functions in Scala. We also go through the 
definition of a functional programming subset and what benefits do we get from using it.

Goals:
* Getting familiar with the various function encodings in Scala (val/def, curried/uncurried, lazy/strict parameters).
* Understand rules of type inference.
* Learn to detect if a function satisfies the functional programming subset.

### Module 2: [Side Effect](https://fp-tower.github.io/foundation/2-SideEffect.html#1)

We tackle the workarounds functional programmers found to interact with the outside world while respecting
the limitation of pure functions. In this module, we examine Thunk, IO and restricted algebra. 
We also have some initiation to concurrency in Scala and how it can be incorporated into IO.

Goals:
* Being able to write an entire program following functional programming principles.
* Learn how to test IO and laziness.
* Getting experience working with an API using type parameters.

### Part 2: Errors

### Module 3: [Error Handling](https://fp-tower.github.io/foundation/3-ErrorHandling.html#1)

They are many ways to deal with runtime errors. The easiest and most common method consists of throwing
exceptions. Unfortunately, it is not compatible with the functional programming paradigm.
In this module, we look at the problems caused by exceptions and the various ways we can safely encode
errors in Scala.

Goals:
* Learn when to use Option, Try and Either.
* Getting comfortable with error encoding using ADT and its various trade-offs.

### Module 4: [Type](https://fp-tower.github.io/foundation/4-Type.html#1)

We all have tendencies to use types that are too large for our purpose. This leads to either runtime 
exceptions or a lot of error handling logic. In this module, we look at using Algebraic Data Types (ADT) and
type parameters to constraint our programs.

Goals:
* Learn how to encode business requirements in types.
* Start to get familiar with Scala type hierarchy and type parameters.


## FAQ

### Where should I start?

Each module has:
* a set of slides (linked above) located at `docs/$module.html`
* one or more Scala objects containing exercises located at `exercises/src/main/scala/exercises/$module/...Exercises.scala`
* one or more Scala objects containing all answers located at `exercises/src/main/scala/answers/$module/...Answers.scala`
* two test suites (one for exercises, one for answers) located at `exercises/src/test/scala/exercises/$module/...Test.scala`

Start each module with the slides, at some point you will be prompted to work on some exercises. 
Do the exercises and check your answers are correct by running the tests for this module.
Finally, go back to the slides and repeat the process. If you have any question, please ask them on [gitter][gitter].

### How to run the tests?

```bash
sbt testExercises$X
```

For example tests for Error Handling module (module 3) is run with:

```bash
sbt testExercises3
```

### Is this course free?

Yes, the material is open source and it uses a permissive licence [CC-BY-SA-4.0][licence].
However, the slides and exercises are currently made to be run by a trainer. Eventually, we aim to create an online 
version of this course. It will most likely be available mid or end of 2020.

We do encourage people who enjoy the course to make a donation using [patreon][patreon].

[gitter]: https://gitter.im/fp-tower/foundation?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge
[licence]: https://creativecommons.org/licenses/by-sa/4.0/legalcode
[patreon]: https://www.patreon.com/bePatron?u=10482033