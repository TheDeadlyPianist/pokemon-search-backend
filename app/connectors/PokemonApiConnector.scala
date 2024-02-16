package connectors

import config.AppConfig
import models.{ErrorModel, PokemonApiModel}
import play.api.Logging
import play.api.http.Status.{BAD_REQUEST, OK}
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class PokemonApiConnector @Inject()(wsClient: WSClient, appConfig: AppConfig)(implicit ec: ExecutionContext) extends Logging {

  def searchPokemon(pokemonName: String): Future[Either[ErrorModel, PokemonApiModel]] = {
    wsClient
      .url(appConfig.pokemonApiUrl + pokemonName)
      .get()
      .map { response =>
        response.status match {
          case OK =>
            val bodyAsJson = Json.parse(response.body)
            val jsonAsModel = bodyAsJson.validate[PokemonApiModel]
            
            jsonAsModel.fold(
              errors => {
                val errorMessages = errors.map { error =>
                  error._1 -> error._2.map(_.message)
                }
                logger.error("[PokemonApiConnector][searchPokemon] Error while parsing the returned JSON.\nParsing errors:\n" + errorMessages)
                Left(ErrorModel(BAD_REQUEST, "Unable to parse JSON from the Pokemon API."))
              },
              pokemonApiModel => {
                logger.info("[PokemonApiConnector][searchPokemon] Got a successful response from the Pokemon API.")
                Right(pokemonApiModel)
              }
            )
          case errorStatus =>
            logger.error("[PokemonApiConnector][searchPokemon] An error has been returned from the Pokemon API. Status: " + errorStatus)
            Left(ErrorModel(errorStatus, "Error with the request to the Pokemon API."))
        }
      }
  }
}
