package exercises.dataprocessing

import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import exercises.dataprocessing.JsonExercises._

class JsonExercisesTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {
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

  ignore("anonymize") {
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

  ignore("depth") {
    assert(depth(JsonNumber(1)) == 0)
    assert(depth(JsonObject(Map.empty)) == 0)
    assert(depth(JsonObject(Map("k" -> JsonNumber(1)))) == 1)
    assert(depth(john) == 2)
  }

}
