package models

import play.api.libs.json.{JsPath, Json, OWrites, Reads}

case class PokemonApiModel(name: String, frontSpriteUrl: String, typing: Seq[String])

object PokemonApiModel {
  implicit val writes: OWrites[PokemonApiModel] = Json.writes[PokemonApiModel]
  
  private val nameRead = (JsPath \ "type" \ "name").read[String]
  private val typesRead = Reads.seq(nameRead)
  
  implicit val reads: Reads[PokemonApiModel] = {
    for {
      name <- (JsPath \ "name").read[String]
      frontSprite <- (JsPath \ "sprites" \ "front_default").read[String]
      types <- (JsPath \ "types").read(typesRead)
    } yield {
      PokemonApiModel(name, frontSprite, types)
    }
  }
}
