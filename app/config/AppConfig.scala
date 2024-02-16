package config

import com.google.inject.ImplementedBy
import play.api.Configuration

import javax.inject.Inject

class AppConfigImpl @Inject()(configuration: Configuration) extends AppConfig {
  override val pokemonApiUrl: String = configuration.get[String]("urls.api.pokemon")
}

@ImplementedBy(classOf[AppConfigImpl])
trait AppConfig {
  val pokemonApiUrl: String
}
