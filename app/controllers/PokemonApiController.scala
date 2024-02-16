package controllers

import play.api.libs.json.Json
import play.api.mvc._
import services.PokemonApiService

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class PokemonApiController @Inject()(
                                      val controllerComponents: ControllerComponents,
                                      service: PokemonApiService
                                    )(implicit ec: ExecutionContext) extends BaseController {
  
  def searchPokemon(pokemonName: String): Action[AnyContent] = Action.async {
    service.searchForPokemon(pokemonName).map {
      case Left(errorModel) => Status(errorModel.status)(Json.toJson(errorModel))
      case Right(pokemonApiModel) => Ok(Json.toJson(pokemonApiModel))
    }
  }
}
