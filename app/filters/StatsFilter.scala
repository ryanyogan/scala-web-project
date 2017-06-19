package filters
import play.api.Logger
import play.api.mvc.{Result, RequestHeader, Filter}
import akka.stream.Materializer
import akka.actor.ActorSystem

import scala.concurrent.Future

class StatsFilter(implicit val mat: Materializer) extends Filter {
  override def apply(nextFilter: (RequestHeader) => Future[Result])
                    (header: RequestHeader): Future[Result] = {
    Logger.info(s"Serving another request: ${header.path}")
    nextFilter(header)
  }
}