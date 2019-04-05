package typeclass

import exercises.typeclass.{MyId, Monoid}
import org.scalatest.{FunSuite, Matchers}
import org.typelevel.discipline.scalatest.Discipline

class TypeclassTest extends FunSuite with Discipline with Matchers {

  test("check Double instance"){
    import exercises.typeclass.TypeclassExercises._
    val p = Monoid[Double]

    p.plus(p.plus(2.0, 3.5), p.zero) shouldEqual 3.5
  }


  test("check MyId instance"){
    import exercises.typeclass.TypeclassExercises._
    val p = Monoid[MyId]

    p.plus(p.plus(MyId("foo"), MyId("bar")), p.zero) shouldEqual MyId("foobar")
  }

  test("check List instance"){
    import exercises.typeclass.TypeclassExercises._
    val p = Monoid[List[Int]]

    p.plus(p.plus(List(1,2,3), List(4,5)), p.zero) shouldEqual List(1,2,3,4,5)
  }

  test("check (Int, String) instance"){
    import exercises.typeclass.TypeclassExercises._
    val p = Monoid[(Int, String)]

    p.plus(p.plus((3, "Hello"), (5, "World")), p.zero) shouldEqual ((8, "HelloWorld"))
  }

  test("check (Int, String) instance"){
    import exercises.typeclass.TypeclassExercises._
    val p = Monoid[(Int, Int)]

    p.plus(p.plus((3, 5), (5, 1)), p.zero) shouldEqual ((8, 6))
  }

  test("check Option instance"){
    import exercises.typeclass.TypeclassExercises._
    val p = Monoid[Option[Int]]

    p.plus(p.plus(Some(3), Some(4)), p.zero) shouldEqual Some(7)
  }


  test("check instances summonable"){
    "Monoid[Int]" should compile
    "Monoid[Double]" should compile
    "Monoid[Float]" should compile
    "Monoid[MyId]" should compile
    "Monoid[(Int, String)]" should compile
  }

  test("check String instance insert a single space when plus"){
    Monoid[String].plus("Hello", "World") shouldEqual "Hello World"
  }

  checkAll("Int", MonoidLaws[Int])
  checkAll("String", MonoidLaws[String])

  checkAll("Int", MonoidLaws.strong[Int])
}
