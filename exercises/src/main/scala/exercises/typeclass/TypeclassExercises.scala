package exercises.typeclass

object TypeclassApp extends App {
  import TypeclassExercises._

  println(2 + 2)
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


  // 2. move above typeclass instance such as you don't need to import TypeclassExercises._ to access them


}
