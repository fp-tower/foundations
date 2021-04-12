package answers.action.fp.game

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class GameServiceTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  val playerIdGen: Gen[PlayerId] = arbitrary[Long].map(PlayerId)

  val playerGen: Gen[Player] =
    for {
      id   <- playerIdGen
      name <- Gen.alphaNumStr
    } yield Player(id, name)

}
