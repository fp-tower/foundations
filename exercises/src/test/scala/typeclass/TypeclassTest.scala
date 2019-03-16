package typeclass

import org.scalatest.{FreeSpec, Matchers}
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import exercises.typeclass.{MyId, Plusable}

class TypeclassTest extends FreeSpec with Matchers with GeneratorDrivenPropertyChecks {

  "check Double instance" in {
    import exercises.typeclass.TypeclassExercises._
    val p = Plusable[Double]

    p.plus(p.plus(2.0, 3.5), p.zero) shouldEqual 3.5
  }

  "check Float instance" in {
    import exercises.typeclass.TypeclassExercises._
    val p = Plusable[Float]

    p.plus(p.plus(2.0f, 3.5f), p.zero) shouldEqual 3.5f
  }


  "check MyId instance" in {
    import exercises.typeclass.TypeclassExercises._
    val p = Plusable[MyId]

    p.plus(p.plus(MyId("foo"), MyId("bar")), p.zero) shouldEqual MyId("foobar")
  }

  "check (Int, String) instance" in {
    import exercises.typeclass.TypeclassExercises._
    val p = Plusable[(Int, String)]

    p.plus(p.plus((3, "Hello"), (5, "World")), p.zero) shouldEqual ((8, "HelloWorld"))
  }


  "check instances summonable" in {
    "Plusable[Int]" should compile
    "Plusable[Double]" should compile
    "Plusable[Float]" should compile
    "Plusable[MyId]" should compile
    "Plusable[(Int, String)]" should compile
  }
}
