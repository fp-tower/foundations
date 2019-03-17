package exercises.typeclass

import cats.data.NonEmptyList

object TypeclassApp extends App {
  import TypeclassExercises._

  println(doublePlusable.plus(0.3, 10.7))
}

object TypeclassExercises {

  // 1a. Implement an instance of Plusable for Double
  implicit val doublePlusable: Plusable[Double] = new Plusable[Double] {
    def plus(a1: Double, a2: Double): Double = ???
    def zero: Double = ???
  }

  // 1b. Implement an instance of Plusable for Float
  implicit val floatPlusable: Plusable[Float] = new Plusable[Float] {
    def plus(a1: Float, a2: Float): Float = ???
    def zero: Float = ???
  }

  // 1c. Implement an instance of Plusable for MyId
  implicit val myIdPlusable: Plusable[MyId] = new Plusable[MyId] {
    def plus(a1: MyId, a2: MyId): MyId = ???
    def zero: MyId = ???
  }

  // 1d. Implement an instance of Plusable for (Int, String)
  implicit val intStringPlusable: Plusable[(Int, String)] = new Plusable[(Int, String)] {
    def plus(a1: (Int, String), a2: (Int, String)): (Int, String) = ???
    def zero: (Int, String) = ???
  }

  // 1e. Implement an instance of Plusable for Either[Int, String]
  implicit val intOrStringPlusable: Plusable[Either[Int, String]] = new Plusable[Either[Int, String]] {
    def plus(a1: Either[Int, String], a2: Either[Int, String]): Either[Int, String] = ???
    def zero: Either[Int, String] = ???
  }

  // 1f. Implement an instance of Plusable for  List
  implicit def listPlusable[A]: Plusable[List[A]] = new Plusable[List[A]] {
    def plus(a1: List[A], a2: List[A]): List[A] = ???
    def zero: List[A] = ???
  }


  // 1g. Implement an instance of Plusable for Boolean
  implicit val booleanPlusable: Plusable[Boolean] = new Plusable[Boolean] {
    def plus(a1: Boolean, a2: Boolean): Boolean = ???
    def zero: Boolean = ???
  }

  // 1h. Implement an instance of Plusable for Option
  implicit def optionPlusable[A]: Plusable[Option[A]] = new Plusable[Option[A]] {
    def plus(a1: Option[A], a2: Option[A]): Option[A] = ???
    def zero: Option[A] = ???
  }

  // 1i. Implement an instance of Plusable for NonEmptyList
  implicit def nelPlusable[A]: Plusable[NonEmptyList[A]] = new Plusable[NonEmptyList[A]] {
    def plus(a1: NonEmptyList[A], a2: NonEmptyList[A]): NonEmptyList[A] = ???
    def zero: NonEmptyList[A] = ???
  }

  // 1j. Implement an instance of Plusable for Map
  implicit def mapPlusable[K, A]: Plusable[Map[K, A]] = new Plusable[Map[K, A]] {
    def plus(a1: Map[K, A], a2: Map[K, A]): Map[K, A] = ???
    def zero: Map[K, A] = ???
  }


  // 2. move above typeclass instance such as you don't need to import TypeclassExercises._ to access them


  // 3a. Refactor String instance of Plusable such as plus add a single space in between


  // 3b. What properties Plusable have? What can you say about plus and empty for all A?
  // Implement these properties to PlusableLaws and check your instances pass those laws


  // 3c. What property Plusable[Int] or Plusable[Boolean] have but say Plusable[String] doesn't
  // Add it to StrongPlusableLaws


  // 3d. Create a weaker typeclass such as it is possible to "plus" NonEmptyList
  // What properties does it have? How does it integrate with Plusable?


}
