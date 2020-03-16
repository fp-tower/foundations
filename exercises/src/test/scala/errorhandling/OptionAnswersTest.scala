package errorhandling

import answers.errorhandling.OptionAnswers.Role._
import answers.errorhandling.OptionAnswers._
import answers.sideeffect.{IOAsync, IOAsyncRef}
import exercises.errorhandling.InvOption
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.concurrent.duration._

class OptionAnswersTest extends AnyFunSuite with Matchers {

  test("getUserEmail") {
    val userMap = Map(
      UserId(222) -> User(UserId(222), "john", Some(Email("j@x.com"))),
      UserId(123) -> User(UserId(123), "elisa", Some(Email("e@y.com"))),
      UserId(444) -> User(UserId(444), "bob", None)
    )

    getUserEmail(UserId(123), userMap) shouldEqual Some(Email("e@y.com"))
    getUserEmail(UserId(444), userMap) shouldEqual None
    getUserEmail(UserId(111), userMap) shouldEqual None
  }

  test("optSingleAccountId") {
    Editor(AccountId(123), "Comic Sans").optSingleAccountId shouldEqual Some(AccountId(123))
    Reader(AccountId(123), premiumUser = true).optSingleAccountId shouldEqual Some(AccountId(123))
    Admin.optSingleAccountId shouldEqual None
  }

  test("optEditor") {
    val editor = Editor(AccountId(123), "Comic Sans")
    editor.optEditor shouldEqual Some(editor)
    Reader(AccountId(123), premiumUser = true).optEditor shouldEqual None
    Admin.optEditor shouldEqual None
  }

  test("parseShape") {
    parseShape("C 5") shouldEqual InvOption.Some(Shape.Circle(5))
    parseShape("R 2 5") shouldEqual InvOption.Some(Shape.Rectangle(2, 5))
    parseShape("R 2") shouldEqual InvOption.None()
    parseShape("C 2 3") shouldEqual InvOption.None()
    parseShape("W 2 5") shouldEqual InvOption.None()
  }

  test("filterDigits") {
    filterDigits("a1bc4".toList) shouldEqual List(1, 4)
  }

  test("checkAllDigits") {
    checkAllDigits("1234".toList) shouldEqual Some(List(1, 2, 3, 4))
    checkAllDigits("a1bc4".toList) shouldEqual None
  }

  test("sendUserEmail") {
    def inMemoryDb(ref: IOAsyncRef[Map[UserId, User]]): DbApi = new DbApi {
      def getAllUsers: IOAsync[Map[UserId, User]] = ref.get
    }
    def inMemoryClient(ref: IOAsyncRef[List[(Email, String)]]): EmailClient = new EmailClient {
      def sendEmail(email: Email, body: String): IOAsync[Unit] =
        ref.update(_ :+ (email, body))
    }

    val ec = scala.concurrent.ExecutionContext.global

    val test = for {
      usersRef  <- IOAsyncRef(Map.empty[UserId, User])
      emailsRef <- IOAsyncRef(List.empty[(Email, String)])
      db     = inMemoryDb(usersRef)
      client = inMemoryClient(emailsRef)
      userId = UserId(10)
      email  = Email("j@foo.com")
      body   = "Hello World"
      user   = User(userId, "John", Some(email))
      waitEmail     <- sendUserEmail(db, client)(userId, body).start(ec)
      _             <- IOAsync.sleep(200.millis)
      expectNoEmail <- emailsRef.get
      _             <- usersRef.update(_ + (userId -> user))
      _             <- waitEmail
      expectEmail   <- emailsRef.get
    } yield {
      expectNoEmail shouldEqual Nil
      expectEmail shouldEqual List(email -> body)
    }

    test.unsafeRun()
  }

}
