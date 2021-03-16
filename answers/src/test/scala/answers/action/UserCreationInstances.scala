package answers.action

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import answers.action.imperative.UserCreationAnswers.dateOfBirthFormatter
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen

import scala.util.Try

trait UserCreationInstances {

  val localDateGen: Gen[LocalDate] =
    Gen
      .choose(LocalDate.MIN.toEpochDay, LocalDate.MAX.toEpochDay)
      .map(LocalDate.ofEpochDay)

  val localDateFormatter: Gen[DateTimeFormatter] =
    Gen.oneOf(DateTimeFormatter.ISO_LOCAL_DATE, dateOfBirthFormatter)

  val invalidYesNoInput: Gen[String] =
    arbitrary[String].filterNot(Set("Y", "N"))

  val invalidDateInput: Gen[String] =
    arbitrary[String].suchThat(date => Try(dateOfBirthFormatter.parse(date)).isFailure)

  val invalidMaxAttempt: Gen[Int] =
    Gen.choose(Int.MinValue, 0)

}
