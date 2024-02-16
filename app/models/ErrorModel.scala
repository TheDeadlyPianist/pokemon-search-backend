package models

import play.api.libs.json.{Json, OWrites}

case class ErrorModel(status: Int, message: String)

object ErrorModel {
  implicit val writes: OWrites[ErrorModel] = Json.writes[ErrorModel]
}
