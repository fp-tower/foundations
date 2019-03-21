package function

import answers.function.FunctionAnswers
import exercises.function.FunctionExercises
import exercises.function.FunctionExercises.Person
import org.scalatest.{FreeSpec, Matchers}
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import toimpl.function.FunctionToImpl

import scala.collection.mutable.ListBuffer

class FunctionAnswersTest   extends FunctionToImplTest(FunctionAnswers)
class FunctionExercisesTest extends FunctionToImplTest(FunctionExercises)

class FunctionToImplTest(impl: FunctionToImpl) extends FreeSpec with Matchers with ScalaCheckDrivenPropertyChecks {
  import impl._

  "identity" in {
    identity(3) shouldEqual 3
    identity("foo") shouldEqual "foo"
  }

  "const" in {
    const("foo")(5) shouldEqual "foo"
    const(5)("foo") shouldEqual 5
  }

  "tripleVal" in {
    tripleVal(5) shouldEqual 15
  }


  "tripleAge" in {
    tripleAge(List(Person("John", 23), Person("Alice", 5))) shouldEqual List(Person("John", 69), Person("Alice", 15))
  }

  "setAge" in {
    setAge(List(Person("John", 23), Person("Alice", 5)), 10) shouldEqual List(Person("John", 10), Person("Alice", 10))
  }

  "noopAge" in {
    val xs = List(Person("John", 23), Person("Alice", 5))
    noopAge(xs) shouldEqual xs
  }

  "apply" in {
    apply((_: Int) + 1, 10) shouldEqual 11
  }

  "doubleInc" in {
    doubleInc(0) shouldEqual 1
    doubleInc(6) shouldEqual 13
  }

  "incDouble" in {
    incDouble(0) shouldEqual 2
    incDouble(6) shouldEqual 14
  }

  "curry" in {
    def plus(x: Int, y: Int): Int = x + y

    curry(plus)(4)(6) shouldEqual 10
  }

  "uncurry" in {
    def plus(x: Int)(y: Int): Int = x + y

    uncurry(plus)(4, 6) shouldEqual 10
  }

  "join" in {
    val reverse: Boolean => Boolean = x => !x
    val zeroOne: Boolean => String = x => if(x) "1" else "0"

    join(zeroOne, reverse)(_ + _.toString)(true) shouldEqual "1false"
  }

  List(sumList _, sumList2 _, sumList3 _).zipWithIndex.foreach{ case (f, i) =>
    s"sumList $i small" in {
      f(List(1,2,3,10)) shouldEqual 16
      f(Nil) shouldEqual 0
    }
  }

  List(sumList2 _, sumList3 _).zipWithIndex.foreach { case (f, i) =>
    s"sumList $i large" in {
      val xs = 1.to(1000000).toList

      f(xs) shouldEqual xs.sum
    }
  }

  "find" in {
    val xs = 1.to(1000000).toList

    val seen = ListBuffer.empty[Int]

    val res = find(xs){ x => seen += x; x > 10}

    res shouldEqual Some(11)
    seen.size shouldEqual 11
  }

  "memoize" in {
    def inc(x: Int): Int = x + 1
    def circleCircumference(radius: Int): Double = 2 * radius * Math.PI

    forAll((x: Int) => memoize(inc)(x) shouldEqual inc(x))
    forAll((x: Int) => memoize(circleCircumference)(x) shouldEqual circleCircumference(x))
  }

}
