package services

import connectors.PokemonApiConnector
import models.{ErrorModel, PokemonApiModel}

import javax.inject.Inject
import scala.concurrent.Future

class PokemonApiService @Inject()(connector: PokemonApiConnector) {
  
  def searchForPokemon(pokemonName: String): Future[Either[ErrorModel, PokemonApiModel]] = {
    connector.searchPokemon(pokemonName)
  }
  
}
