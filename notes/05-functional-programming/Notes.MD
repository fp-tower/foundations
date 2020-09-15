
## Definition

A function is an immutable mapping between 2 set of values. This means:
* no actions
* no exceptions
* no mutations
* no randomness
   
## Benefits

* powerful refactoring
* local reasoning (only depends on input)
* compose (pipeline)
* code optimisations (cachable, fusion) 

## Inconveniences

You cannot do anything: 
* lookup or insert values in a database
* send an http request
* read a file
* log a message
   
## Types

Not mandatory but ... 

## Miscellaneous

Definition of a side effect is not clear, for example:
* allocating an object on the heap is not considered a side effect even though it 
consumes a finite resources and could lead to an out of memory error (OOM). 
* reading a config file within a jar is considered a side effect even though the
file was available at compilation.

Immutability is not required, for example:
* local variables (only accessible within a function) are completely fine.
* Rust uses a [Substructural type system](https://en.wikipedia.org/wiki/Substructural_type_system) 
to constraint the access of resources.


FP in the large, imperative in the small