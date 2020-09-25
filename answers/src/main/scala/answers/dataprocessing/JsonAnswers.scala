package answers.dataprocessing

object JsonAnswers {

  sealed trait Json
  case class JsonNumber(number: Double)         extends Json
  case class JsonString(text: String)           extends Json
  case class JsonObject(obj: Map[String, Json]) extends Json
  case class JsonArray(array: List[Json])       extends Json
  case object JsonNull                          extends Json

  def trimAll(json: Json): Json =
    json match {
      case _: JsonNumber   => json
      case JsonString(str) => JsonString(str.trim)
      case JsonObject(obj) =>
        JsonObject(obj.map {
          case (key, value) => key -> trimAll(value)
        })
      case JsonArray(array) => JsonArray(array.map(trimAll))
      case JsonNull         => JsonNull
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
      case JsonArray(array) => JsonArray(array.map(anonymize))
      case JsonNull         => JsonNull
    }

  def search(json: Json, searchText: String, maxDepth: Int): Boolean =
    if (maxDepth < 0) false
    else
      json match {
        case _: JsonNumber | JsonNull => false
        case JsonString(text)         => text.contains(searchText)
        case JsonObject(obj)          => obj.values.exists(search(_, searchText, maxDepth - 1))
        case JsonArray(array)         => array.exists(search(_, searchText, maxDepth - 1))
      }

  def depth(json: Json): Int =
    json match {
      case _: JsonNumber | _: JsonString | JsonNull =>
        0
      case JsonObject(obj) =>
        obj.values.map(depth).maxOption.fold(0)(_ + 1)
      case JsonArray(array) =>
        array.map(depth).maxOption.fold(0)(_ + 1)
    }

}
