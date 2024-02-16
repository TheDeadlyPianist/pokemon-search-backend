package filters

import akka.stream.Materializer
import models.ErrorModel
import play.api.http.Status.{BAD_REQUEST, UNAUTHORIZED}
import play.api.libs.json.Json
import play.api.mvc.Results.{BadRequest, Unauthorized}
import play.api.mvc.{Filter, RequestHeader, Result}

import javax.inject.Inject
import scala.concurrent.Future

class OriginatorIdFilter @Inject()(implicit val mat: Materializer) extends Filter {

  override def apply(f: RequestHeader => Future[Result])(rh: RequestHeader): Future[Result] = {
    
    try {
      val originatorId: String = rh.headers.apply("originator-id")
      
      if(originatorId == "poke-search") {
        f(rh)
      } else {
        Future.successful(Unauthorized(
          Json.toJson(ErrorModel(UNAUTHORIZED, "Originator ID is invalid."))
        ))
      }
    } catch { _ =>
      Future.successful(BadRequest(
        Json.toJson(ErrorModel(BAD_REQUEST, "Originator ID missing."))
      ))
    }
    
  }

}
