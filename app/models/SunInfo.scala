package model

import play.api.libs.json.Json

case class SunInfo(sunrise: String, sunset: String)

// This is a nice way to overwrite / add to a case class
object SunInfo {
  implicit val writes = Json.writes[SunInfo]
}