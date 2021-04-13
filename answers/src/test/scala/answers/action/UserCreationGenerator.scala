package answers.action

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDate}

import answers.action.imperative.UserCreationAnswers.dateOfBirthFormatter
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

import scala.util.Try

object UserCreationGenerator {

  val localDateFormatter: Gen[DateTimeFormatter] =
    Gen.oneOf(DateTimeFormatter.ISO_LOCAL_DATE, dateOfBirthFormatter)

  val invalidYesNoGen: Gen[String] =
    arbitrary[String].filterNot(Set("Y", "N"))

  val invalidDateGen: Gen[String] =
    arbitrary[String].suchThat(date => Try(dateOfBirthFormatter.parse(date)).isFailure)

  val validMaxAttemptGen: Gen[Int]   = Gen.choose(1, 20)
  val invalidMaxAttemptGen: Gen[Int] = Gen.choose(Int.MinValue, 0)

}
