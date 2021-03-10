package answers.action.v2

import java.time.{Instant, LocalDate}
import java.time.format.DateTimeFormatter

import answers.action.v2.UserCreationAnswers._
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Try}

class UserCreationAnswersTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  val localDateGen: Gen[LocalDate] =
    Gen
      .choose(LocalDate.MIN.toEpochDay, LocalDate.MAX.toEpochDay)
      .map(LocalDate.ofEpochDay)

  val localDateFormatter: Gen[DateTimeFormatter] =
    Gen.oneOf(DateTimeFormatter.ISO_LOCAL_DATE, dobFormatter)

  val invalidAttempts: Gen[List[String]] =
    Gen.listOf(Arbitrary.arbitrary[String])

  val invalidMaxAttempt: Gen[Int] =
    Gen.choose(Int.MinValue, 0)

  checkReadDateOfBirth("readDateOfBirth", readDateOfBirth)
  checkReadDateOfBirth("readDateOfBirthV2", readDateOfBirthV2)

  checkReadSubscribeToMailingList("readSubscribeToMailingList", readSubscribeToMailingList)
  checkReadSubscribeToMailingList("readSubscribeToMailingListV2", readSubscribeToMailingListV2)

  def checkReadDateOfBirth(name: String, impl: (Console, DateTimeFormatter, Int) => LocalDate): Unit = {
    test(s"$name success") {
      forAll(localDateFormatter, localDateGen) { (formatter, date) =>
        val dateStr = formatter.format(date)
        val console = Console.mock(ListBuffer(dateStr), ListBuffer.empty)
        val result  = impl(console, formatter, 1)

        assert(result == date)
      }
    }

    test(s"$name retry") {
      forAll(localDateFormatter, localDateGen, invalidAttempts) { (formatter, date, attempts) =>
        val dateStr    = formatter.format(date)
        val inputs     = ListBuffer.from(attempts :+ dateStr)
        val outputs    = ListBuffer.empty[String]
        val console    = Console.mock(inputs, outputs)
        val maxAttempt = attempts.size + 1

        val result = impl(console, formatter, maxAttempt)

        assert(result == date)

        val pairOutput = List(
          "What's your date of birth (dd-mm-yyyy)?",
          """Incorrect format, for example enter "18-03-2001" for 18th of March 2001"""
        )

        val expectedOutputs = 1.to(maxAttempt).flatMap(_ => pairOutput).toList.dropRight(1)
        assert(outputs.toList == expectedOutputs)
      }
    }

    test(s"$name if maxAttempt <= 0") {
      forAll(localDateFormatter, invalidMaxAttempt) { (formatter, maxAttempt) =>
        val console = Console.mock(ListBuffer.empty, ListBuffer.empty)
        val result  = Try(impl(console, formatter, maxAttempt))

        assert(result.isFailure)
      }
    }
  }

  def checkReadSubscribeToMailingList(name: String, impl: (Console, Int) => Boolean): Unit = {
    test(s"$name retry") {
      forAll(Arbitrary.arbitrary[Boolean], invalidAttempts) { (bool, attempts) =>
        val boolStr    = if (bool) "Y" else "N"
        val inputs     = ListBuffer.from(attempts :+ boolStr)
        val outputs    = ListBuffer.empty[String]
        val console    = Console.mock(inputs, outputs)
        val maxAttempt = attempts.size + 1

        val result = impl(console, maxAttempt)

        assert(result == bool)

        val pairOutput = List(
          "Would you like to subscribe to our mailing list? [Y/N]",
          s"""Incorrect format, enter "Y" for Yes or "N" for "No"""""
        )

        val expectedOutputs = 1.to(maxAttempt).flatMap(_ => pairOutput).toList.dropRight(1)
        assert(outputs.toList == expectedOutputs)
      }
    }

    test(s"$name if maxAttempt <= 0") {
      forAll(invalidMaxAttempt) { (maxAttempt) =>
        val console = Console.mock(ListBuffer.empty, ListBuffer.empty)
        val result  = Try(impl(console, maxAttempt))

        assert(result.isFailure)
      }
    }
  }

  test("readUser") {
    val inputs  = ListBuffer("Bob", "12-03-1997", "Y")
    val now     = Instant.now()
    val console = Console.mock(inputs, ListBuffer.empty)
    val clock   = Clock.constant(now)

    val user = readUser(console, clock)(
      readDateOfBirthV2(_, dobFormatter, 2),
      readSubscribeToMailingListV2(_, 2)
    )

    val expectedUser = User(
      name = "Bob",
      dateOfBirth = LocalDate.of(1997, 3, 12),
      subscribedToMailingList = true,
      createdAt = now
    )

    assert(user == expectedUser)
  }

  test("retry when block always succeeds") {
    var counter = 0
    val result = retry(1) { () =>
      counter += 1
      2 + 2
    }
    assert(result == 4)
    assert(counter == 1)
  }

  test("retry when block always fails") {
    forAll { (error: Exception) =>
      var counter = 0
      def exec(): Int = {
        counter += 1
        throw error
      }
      val result = Try(retry(5)(exec))

      assert(result == Failure(error))
      assert(counter == 5)
    }
  }

  test("retry when block fails and then succeeds") {
    var counter = 0
    def exec(): String = {
      counter += 1
      if (counter < 3) throw new Exception("Boom!")
      else "Hello"
    }
    val result = retry(5)(exec)

    assert(result == "Hello")
    assert(counter == 3)
  }

  test("retryWithError when block always succeeds") {
    var counter = 0
    val result = retryWithError(1)(
      block = 2 + 2,
      onError = _ => counter += 1
    )
    assert(result == 4)
    assert(counter == 0)
  }

  test("retryWithError when block always fails") {
    var counter = 0
    val result  = Try(retryWithError(5)(block = throw new Exception("boom"), onError = _ => counter += 1))

    assert(result.isFailure)
    assert(counter == 5)
  }

  test("retryWithError when block fails and then succeeds") {
    var counter = 0
    val result = retryWithError(5)(
      block = if (counter >= 3) "" else throw new Exception("boom"),
      onError = _ => counter += 1
    )

    assert(result == "")
    assert(counter == 3)
  }

}
