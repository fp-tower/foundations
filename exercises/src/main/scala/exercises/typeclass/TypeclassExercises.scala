package exercises.typeclass

import cats.data.NonEmptyList

object TypeclassApp extends App {
  import TypeclassExercises._
  import Plusable.syntax._

  println(0.3.plus(10.7))
}

object TypeclassExercises {

  // 1. Basic instances

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
  implicit val intAndStringPlusable: Plusable[(Int, String)] = new Plusable[(Int, String)] {
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

  // 1g. Implement an instance of Plusable for LoggedValue
  implicit def loggedValuePlusable[A]: Plusable[LoggedValue[A]] = new Plusable[LoggedValue[A]] {
    def plus(a1: LoggedValue[A], a2: LoggedValue[A]): LoggedValue[A] = ???
    def zero: LoggedValue[A] = ???
  }


  // 2. Plusable usage

  // 2a. Implement fold
  def fold[A](xs: List[A])(implicit ev: Plusable[A]): A = ???


  val words = List("Plusable", "are", "awesome", "!")
  val upTo10 = 0.to(10).toList

  // 2b. Use fold to sum up the even number from 0 and 10
  def evenFrom0To10: Int = ???


  // 2c. Implement intercalate
  def intercalate[A](xs: List[A], before: A, between: A, after: A)(implicit ev: Plusable[A]): A = ???


  def intercalate[A](xs: List[A], between: A)(implicit ev: Plusable[A]): A = ???


  // 2d. Use intercalate on words to produce "(Plusable_Are_Awesome_!)"
  def awesomeString: String = ???

  // 2e. Implement folddMap
  def foldMap[A, B](xs: List[A])(f: A => B)(implicit ev: Plusable[B]): B = ???


  // 2f. Re-implement fold in terms of foldMap


  // 2g. Use foldMap to calculate the average words length in words


  // 3. Typeclass hierarchy

  // 3a. Implement an instance of Plusable for NonEmptyList
  implicit def nelPlusable[A]: Plusable[NonEmptyList[A]] = new Plusable[NonEmptyList[A]] {
    def plus(a1: NonEmptyList[A], a2: NonEmptyList[A]): NonEmptyList[A] = ???
    def zero: NonEmptyList[A] = ???
  }

  // 3b. Create a weaker typeclass such as it is possible to "plus" NonEmptyList


  // 4. Advanced instances

  // 4a. Implement an instance of Plusable for Boolean
  implicit val booleanPlusable: Plusable[Boolean] = new Plusable[Boolean] {
    def plus(a1: Boolean, a2: Boolean): Boolean = ???
    def zero: Boolean = ???
  }

  // 4b. Implement an instance of Plusable for Option
  implicit def optionPlusable[A]: Plusable[Option[A]] = new Plusable[Option[A]] {
    def plus(a1: Option[A], a2: Option[A]): Option[A] = ???
    def zero: Option[A] = ???
  }

  // 4c. Implement an instance of Plusable for Map
  implicit def mapPlusable[K, A]: Plusable[Map[K, A]] = new Plusable[Map[K, A]] {
    def plus(a1: Map[K, A], a2: Map[K, A]): Map[K, A] = ???
    def zero: Map[K, A] = ???
  }

  // 4d. Refactor String instance of Plusable such as plus add a single space in between


  // 4e. What properties Plusable have? What can you say about plus and empty for all A?
  // Implement these properties to PlusableLaws and check your instances pass those laws


  // 4f. What property Plusable[Int] or Plusable[Boolean] have but say Plusable[String] doesn't
  // Add it to StrongPlusableLaws



  // 5. move above typeclass instance such as you don't need to import TypeclassExercises._ to access them


}
