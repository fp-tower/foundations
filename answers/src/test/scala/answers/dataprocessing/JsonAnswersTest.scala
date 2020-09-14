package answers.dataprocessing

import answers.dataprocessing.JsonAnswers._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class JsonAnswersTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {
  val john: Json = JsonObject(
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

  test("upperCase") {
    assert(
      upperCase(john) == JsonObject(
        Map(
          "name" -> JsonString("JOHN DOE"),
          "age"  -> JsonNumber(25),
          "address" -> JsonObject(
            Map(
              "street-number" -> JsonNumber(25),
              "street-name"   -> JsonString("CODY ROAD"),
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

  test("depth") {
    assert(depth(JsonNumber(1)) == 0)
    assert(depth(JsonObject(Map.empty)) == 0)
    assert(depth(JsonObject(Map("k" -> JsonNumber(1)))) == 1)
    assert(depth(john) == 2)
  }

}
