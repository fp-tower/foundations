# Synopsis

## 1. Value functions

### Objectives
* Learn when to use methods and lambdas
* Introduction to property-based testing (PBT)

### Lessons
1. Val vs Def functions
1. Quiz: Val vs Def
1. Functions as input
1. Exercises

## 2. Generic functions

### Objectives
* Why we shouldn't pattern on a generic value
* How to use type parameters to write fewer and more efficient tests
* How to use ??? and type parameters to effortlessly implement generic functions
* Introduction to functional API design

### Lessons
1. Part 1
1. Part 2
1. Exercise 1: Pair
1. Exercise 2: Predicate
1. Exercise 3: JsonDecoder

## 3. Case study: Parallel data processing

Modern applications need to process more and more data quickly, which lead to the creation of 
modern technologies such as MapReduce, Spark or Kafka. In this section, we will cover the fundamentals 
of parallel data processing and why functional programming is such a good fit for it. And at 
the end of this section, we will apply everything we have seen on a real-world dataset of worldwide temperature.


### Objectives
* Why FP is a good fit for data processing?
* How to use generic types and property-based testing to explore a problem
* Both mutations and recursions are not safe
* Learn about foldLeft, foldMap and Monoid
* Learn the difference between parallel and concurrent
* Introduction to Scala Future from the standard library

### Lessons
1. Introduction to parallel data processing
1. For loop
1. Recursions
1. World temperature project

## 4. What is functional programming?

### Objectives
* What is functional programming?
* How does it help us write programs?
* What are the inconvenient? 
* When shall we use it or not use it?

### Lessons
1. Definition
1. Quiz: is it functional?
1. Refactoring
1. Shared state

## 5. Actions

### Objectives
* How to perform actions without breaking functional programming principles
* Learn difference between lazy and strict evaluation
* Why Scala Future are not suitable for FP actions
* Introduction to algebra and interpreter to extend an interface

### Lessons
1. ???

## 6a. Error Handling

## 6b. End of course project
