package typeclass

import exercises.typeclass.Monoid.syntax._
import exercises.typeclass.MyId
import exercises.typeclass.TypeclassExercises._
import org.scalatest.{FunSuite, Matchers}
import org.typelevel.discipline.scalatest.Discipline

class TypeclassTest extends FunSuite with Discipline with Matchers {

  test("check Double instance"){
    2.0.combine(3.5).combine(mempty[Double]) shouldEqual 3.5
  }


  test("check MyId instance"){
    MyId("foo").combine(MyId("bar")).combine(mempty[MyId]) shouldEqual MyId("foobar")
  }

  test("check List instance"){
    List(1,2,3).combine(List(4,5)).combine(mempty[List[Int]]) shouldEqual List(1,2,3,4,5)
  }

  test("check (Int, String) instance"){
    (3, "Hello").combine((5, "World")).combine(mempty[(Int, String)]) shouldEqual ((8, "HelloWorld"))
  }

  test("check (Int, Int) instance"){
    (3, 5).combine((5, 1)).combine(mempty[(Int, Int)]) shouldEqual ((8, 6))
  }

  test("check Option instance"){
    Option(3).combine(Option(4)).combine(mempty[Option[Int]]) shouldEqual Some(7)
  }


  test("check instances summonable"){
    "Monoid[Int]" should compile
    "Monoid[Double]" should compile
    "Monoid[Float]" should compile
    "Monoid[MyId]" should compile
    "Monoid[(Int, String)]" should compile
  }

  checkAll("Int", MonoidLaws[Int])
  checkAll("String", MonoidLaws[String])

  checkAll("Int", MonoidLaws.strong[Int])
}
