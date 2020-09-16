package exercises.dataprocessing

object JsonExercises {

  sealed trait Json
  case class JsonNumber(number: Double)         extends Json
  case class JsonString(text: String)           extends Json
  case class JsonObject(obj: Map[String, Json]) extends Json

  def trimAll(json: Json): Json =
    json match {
      case _: JsonNumber   => json
      case JsonString(str) => JsonString(str.trim)
      case JsonObject(obj) =>
        val newObj = obj.map {
          case (key, value) => (key, trimAll(value))
        }
        JsonObject(newObj)
    }

  // a. Implement `anonymize`, a method which keeps the structure of the JSON document
  // but removes all data such as:
  // * all `JsonString` are replaced by `***`.
  // * all `JsonNumbers` are replaced by 0
  // For example:
  // {                                          {
  //  "name": "John Doe",                         "name": "***",
  //  "age": 25,                                  "age": 0,
  //  "address": {                                "address": {
  //    "street": {             anonymize             "street": {
  //      "number" : 12,           ==>                "number" : 0,
  //      "name" : "Cody road"                        "name" : "***"
  //    },                                          },
  //    "country": "UK",                            "country": "***",
  //  }                                           }
  //}                                           }
  def anonymize(json: Json): Json =
    ???

  // b. Implement `search`, a method that check if a JSON document contains a text.
  // For example:
  // * search({ }, "ll") == false
  // * search(5, "ll") == false
  // * search("Hello", "ll") == true
  // * search({ "message" -> "hello" }, "ll") == true
  // * search({ "message" -> "hi" }, "ll") == false
  def search(json: Json, text: String): Boolean =
    ???

  //////////////////////////////////////////////
  // Bonus question (not covered by the video)
  //////////////////////////////////////////////

  // c. Implement `depth`, a method that calculate the maximum level of nesting of a JSON document.
  // For example:
  // * { } has depth 0
  // * { "name" : "john" } has depth 1
  // * { "name" : "john", "address" : { "postcode" : "E16 4SR" } } has depth 2
  def depth(json: Json): Int =
    ???

}
