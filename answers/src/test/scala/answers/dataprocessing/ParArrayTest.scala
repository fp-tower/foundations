package answers.dataprocessing

import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

class ParArrayTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  val ec: ExecutionContextExecutor = ExecutionContext.global

  test("par sum") {
    val parArray = ParArray(ec, 1.to(20).toArray, 10)
    assert(
      parArray.parFoldMap(identity)(Monoid.sumNumeric) == parArray.parFoldMap(identity)(Monoid.sumNumeric)
    )
  }

}
