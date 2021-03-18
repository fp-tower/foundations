package answers.action

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDate}

import answers.action.imperative.UserCreationAnswers.dateOfBirthFormatter
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

import scala.util.Try

trait UserCreationInstances {

  val localDateGen: Gen[LocalDate] =
    Gen
      .choose(LocalDate.MIN.toEpochDay, LocalDate.MAX.toEpochDay)
      .map(LocalDate.ofEpochDay)

  implicit val localDateArb: Arbitrary[LocalDate] =
    Arbitrary(localDateGen)

  val instantGen: Gen[Instant] =
    for {
      seconds <- Gen.choose(Instant.MIN.getEpochSecond, Instant.MAX.getEpochSecond)
      nano    <- Gen.choose(0, 1000_000_000L)
    } yield Instant.ofEpochSecond(seconds, nano)

  implicit val instantArb: Arbitrary[Instant] =
    Arbitrary(instantGen)

  val localDateFormatter: Gen[DateTimeFormatter] =
    Gen.oneOf(DateTimeFormatter.ISO_LOCAL_DATE, dateOfBirthFormatter)

  val invalidYesNoGen: Gen[String] =
    arbitrary[String].filterNot(Set("Y", "N"))

  val invalidDateInput: Gen[String] =
    arbitrary[String].suchThat(date => Try(dateOfBirthFormatter.parse(date)).isFailure)

  val validMaxAttempt: Gen[Int] = Gen.choose(1, 20)
  val invalidMaxAttempt: Gen[Int] = Gen.choose(Int.MinValue, 0)

}
