package function

import org.scalatest.{FunSuite, Matchers}
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

import scala.util.Random


class PureFunctionExercisesTest extends FunSuite with ScalaCheckDrivenPropertyChecks with Matchers with Discipline {

  def increment(x: Int): Int = x + 1

  def incrementRandom(x: Int): Int = {
    if(x > 100000) x + Random.nextInt(5)
    else x + 1
  }

  def incrementPartial(x: Int): Int = x match {
    case 0 => 5
    case 1 => 6
    case _ if x > 1 => x + 1
  }

  checkAll("increment", PureFunctionLaws(increment))

  // Should fail for the functions below

//  checkAll("incrementRandom", PureFunctionLaws(incrementRandom))

//  checkAll("incrementPartial", PureFunctionLaws(incrementPartial))


}
