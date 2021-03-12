package exercises.actions

import java.time.{Instant, LocalDate}

import org.scalatest.funsuite.AnyFunSuite

import scala.util.{Failure, Try}
import UserCreationExercises._
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.mutable.ListBuffer

class UserCreationExercisesTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  ignore("readSubscribeToMailingList example") {
    val console = Console.mock(ListBuffer("N"), ListBuffer())
    val result  = readSubscribeToMailingList(console)

    assert(result == false)
  }

  ignore("readDateOfBirth example") {
    val console = Console.mock(ListBuffer("21-07-1986"), ListBuffer())
    val result  = readDateOfBirth(console)

    assert(result == LocalDate.of(1986, 7, 21))
  }

  ignore("readUser example") {
    val inputs  = ListBuffer("Eda", "18-03-2001", "Y")
    val outputs = ListBuffer.empty[String]
    val console = Console.mock(inputs, outputs)
    val result  = readUser(console)

    val expected = User(
      name = "Eda",
      dateOfBirth = LocalDate.of(2001, 3, 18),
//      subscribedToMailingList = true,
      createdAt = Instant.now()
    )

    assert(result == expected)
  }

  ignore("retry when block always succeeds") {
    var counter = 0
    val result = retry(1) { () =>
      counter += 1
      2 + 2
    }
    assert(result == 4)
    assert(counter == 1)
  }

  ignore("retry when block always fails") {
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

}
