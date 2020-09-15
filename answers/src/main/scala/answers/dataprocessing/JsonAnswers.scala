package answers.dataprocessing

object JsonAnswers {

  sealed trait Json
  case class JsonNumber(num: Double)            extends Json
  case class JsonString(str: String)            extends Json
  case class JsonObject(obj: Map[String, Json]) extends Json

  def upperCase(json: Json): Json =
    json match {
      case _: JsonNumber   => json
      case JsonString(str) => JsonString(str.toUpperCase)
      case JsonObject(obj) =>
        JsonObject(obj.map {
          case (key, value) =>
            key -> upperCase(value)
        })
    }

  def anonymize(json: Json): Json =
    json match {
      case _: JsonNumber => JsonNumber(0)
      case _: JsonString => JsonString("***")
      case JsonObject(obj) =>
        JsonObject(obj.map {
          case (key, value) =>
            key -> anonymize(value)
        })
    }

  def depth(json: Json): Int =
    json match {
      case _: JsonNumber | _: JsonString =>
        0
      case JsonObject(obj) =>
        obj.values.map(depth).maxOption.fold(0)(_ + 1)
    }

}
