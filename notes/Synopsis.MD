# Synopsis

## 1. Value functions

In this section, we'll do a deep dive on the different types of functions available in Scala.
What is the difference between them and when should use which one.

### Objectives
* Learn the difference between methods and lambdas
* How to capture a pattern using a higher-order function
* Introduction to property-based testing and common testing techniques
* Learn the various Scala syntax for functions including usage of `_`

## 2. Generic functions

In Scala, we can add type parameters to functions and data types to make them generic. 
This simple technique has a tremendous impact in the design of functional API that we'll use
extensively throughout the course. In this section, we learn how to read and implement interfaces 
using generic types as well as the common pitfalls related to the usage of type parameters.

### Objectives
* How to use type parameters to write fewer and more efficient tests
* Why we shouldn't pattern match on a generic value
* How to work with Scala type inference
* How to use ??? and type parameters to guide the implementation of functions
* Introduction to functional API design
* Introduction to implicits with Gen/Arbitrary from ScalaCheck

## 3. Case study: Parallel data processing

Modern applications need to process more and more data quickly, which lead to the creation of 
technologies such as MapReduce, Spark or Kafka. In this section, we will cover the fundamentals 
of parallel data processing and why functional programming is such a good fit for it. We'll conclude 
this module with the analysis of a real-world dataset of worldwide temperature.

### Objectives
* Why FP is a good fit for data processing
* How to use generic types and property-based testing to explore a problem
* Why mutations and recursions are too low level
* Learn about foldLeft, foldMap and Monoid
* Learn the difference between parallel and concurrent programs
* Introduction to Future from the Scala standard library

## 4. What is functional programming?

What is functional programming all about? What is the single most important idea behind it? 
What are its limitations? And, why isn't it more popular? You will get the answers to all these 
questions and much more in this module. 

### Objectives
* What is functional programming?
* How does it help us write better programs?
* What are the inconvenient? 
* How can we work around its limitations? e.g. shared mutable state or exceptions

## 5. Actions

In the previous section, we saw that functional programming is quite restrictive. For example, we cannot
perform actions like sending an email or updating a record in a database. Yet, we need to these actions 
almost every day in a real word application. In this section, we will discuss the workarounds 
functional programmers found to interact with the outside world while respecting functional programming 
principles. In particular, we will implement a simple IO data type that supports powerful retry strategies
and concurrency primitives.

### Objectives
* How to perform actions without breaking functional programming principles
* Learn the difference between lazy and strict evaluations
* Why the Scala Future is not suitable for FP actions
* Introduction to algebra and interpreter to extend an interface

## 6. Error Handling

There are many ways to deal with runtime errors. The easiest and most common method consists of 
throwing exceptions. Unfortunately, it is not compatible with the functional programming paradigm. 
In this module, we look at the problems caused by exceptions, and the various ways we can safely 
encode errors in Scala.

### Objectives
* Learn about Option and Either data types and when to use them
* What is variance and how can it help type inference?
* How to combine actions (IO) and error handling (Option/Either)
