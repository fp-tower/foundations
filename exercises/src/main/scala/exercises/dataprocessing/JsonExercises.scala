package exercises.dataprocessing

object JsonExercises {

  sealed trait Json
  case class JsonNumber(num: Double)            extends Json
  case class JsonString(str: String)            extends Json
  case class JsonObject(obj: Map[String, Json]) extends Json

  def upperCase(json: Json): Json =
    json match {
      case JsonNumber(num) => JsonNumber(num)
      case JsonString(str) => JsonString(str.toUpperCase)
      case JsonObject(obj) =>
        JsonObject(obj.map {
          case (key, value) =>
            key -> upperCase(value)
        })
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

  // b. Implement `depth`, a method that calculate the maximum level of nesting of a JSON document.
  // For example:
  // { } has depth 0
  // { "name" : "john" } has depth 1
  // { "name" : "john", "address" : { "postcode" : "E16 4SR" } } has depth 2
  def depth(json: Json): Int =
    ???

}
