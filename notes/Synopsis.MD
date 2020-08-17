# Synopsis

## 1. Value functions

In this section, we'll do a deep dive on the different types of functions available in Scala.
What is the difference between them and when should use which one.

### Objectives
* Learn when to difference between methods and lambdas
* How to capture a pattern using a higher-order function
* Introduction to property-based testing and common testing techniques
* Scala syntax for functions including various usage of `_`

## 2. Generic functions

### Objectives
* How to use type parameters to write fewer and more efficient tests
* Why we shouldn't pattern on a generic value
* How to work with Scala type inference
* How to use ??? and type parameters to effortlessly implement generic functions
* Introduction to functional API design
* Introduction to implicits with Gen/Arbitrary from ScalaCheck

## 3. Case study: Parallel data processing

Modern applications need to process more and more data quickly, which lead to the creation of 
modern technologies such as MapReduce, Spark or Kafka. In this section, we will cover the fundamentals 
of parallel data processing and why functional programming is such a good fit for it. And at 
the end of this section, we will apply everything we have seen on a real-world dataset of worldwide temperature.

### Objectives
* Why FP is such a good fit for data processing
* How to use generic types and property-based testing to explore a problem
* Why both mutations and recursions are not safe
* Learn about foldLeft, foldMap and Monoid
* Learn the difference between parallel and concurrent programs
* Introduction to Future from the Scala standard library

## 4. What is functional programming?

What is functional programming all about? What is the single most important idea behind it? 
What are the limitations of functional programming and why isn't it more popular? You will get
the answers to all these questions and much more in this section. 

### Objectives
* What is functional programming?
* How does it help us write better programs?
* What are the inconvenient? 
* How can we work around those limitations? e.g. shared mutable state or exceptions

## 5. Actions

In the previous section, we saw that functional programming is quite restrictive. For example, we cannot
perform actions like sending an email or updating a record in the database. Yet, these are things 
we need to do every day in a real word application. In this section, we will discuss the workarounds 
functional programmers found to interact with the outside world while respecting functional programming 
principles. In particular, we will implement a simple IO data type that supports powerful retry strategies
and concurrency primitives.

### Objectives
* How to perform actions without breaking functional programming principles
* Learn the difference between lazy and strict evaluations
* Why the Scala Future is not suitable for FP actions
* Introduction to algebra and interpreter to extend an interface

## 6a. Error Handling

There are many ways to deal with runtime errors. The easiest and most common method consists of 
throwing exceptions. Unfortunately, it is not compatible with the functional programming paradigm. 
In this module, we look at the problems caused by exceptions, and the various ways we can safely 
encode errors in Scala.

### Objectives
* Learn to use Option and Either and their limitations
* What is variance and how can it help type inference?
* How to combine actions (IO) and error handling

## 6b. End of course project
