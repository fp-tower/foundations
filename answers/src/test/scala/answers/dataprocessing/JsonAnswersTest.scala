package answers.dataprocessing

import answers.dataprocessing.JsonAnswers._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class JsonAnswersTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {
  val john: Json = JsonObject(
    Map(
      "name" -> JsonString(" John Doe "),
      "age"  -> JsonNumber(25),
      "address" -> JsonObject(
        Map(
          "street-number" -> JsonNumber(25),
          "street-name"   -> JsonString("  Cody Road"),
        )
      ),
    )
  )

  test("trimAll") {
    assert(
      trimAll(john) == JsonObject(
        Map(
          "name" -> JsonString("John Doe"),
          "age"  -> JsonNumber(25),
          "address" -> JsonObject(
            Map(
              "street-number" -> JsonNumber(25),
              "street-name"   -> JsonString("Cody Road"),
            )
          ),
        )
      )
    )
  }

  test("anonymize") {
    assert(
      anonymize(john) == JsonObject(
        Map(
          "name" -> JsonString("***"),
          "age"  -> JsonNumber(0),
          "address" -> JsonObject(
            Map(
              "street-number" -> JsonNumber(0),
              "street-name"   -> JsonString("***"),
            )
          ),
        )
      )
    )
  }

  test("search") {
    assert(search(JsonObject(Map.empty), "ll", 5) == false)
    assert(search(JsonNumber(5), "ll", 5) == false)
    assert(search(JsonString("Hello"), "ll", 5) == true)
    assert(search(JsonObject(Map("message" -> JsonString("Hello"))), "ll", 5) == true)
    assert(search(JsonObject(Map("message" -> JsonString("Hello"))), "ss", 5) == false)
    assert(search(JsonObject(Map("message" -> JsonString("hi"))), "ll", 5) == false)

    assert(search(JsonObject(Map("user" -> JsonObject(Map("name" -> JsonString("John"))))), "o", 2) == true)
    assert(search(JsonObject(Map("user" -> JsonObject(Map("name" -> JsonString("John"))))), "o", 1) == false)
  }

  test("depth") {
    assert(depth(JsonNumber(1)) == 0)
    assert(depth(JsonObject(Map.empty)) == 0)
    assert(depth(JsonObject(Map("k" -> JsonNumber(1)))) == 1)
    assert(depth(john) == 2)
  }

}
