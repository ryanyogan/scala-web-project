import controllers.{Application, Assets}
import play.api.ApplicationLoader.Context
import play.api._
import akka.actor.Props
import play.api.routing.Router
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.mvc.{Filter, EssentialFilter}
import router.Routes
import com.softwaremill.macwire._
import services.{SunService, WeatherService}
import filters.StatsFilter
import actors.StatsActor
import actors.StatsActor.Ping

import scala.concurrent.Future

class AppApplicationLoader extends ApplicationLoader {
  def load(context: Context) = {
    LoggerConfigurator(context.environment.classLoader).foreach { conf =>
      conf.configure(context.environment)
    }
    (new BuiltInComponentsFromContext(context) with AppComponents).application
  }
}

trait AppComponents extends BuiltInComponents with AhcWSComponents {
  applicationLifecycle.addStopHook { () =>
    Logger.info("The app is about to shutdown.")
    Future.successful(Unit)
  }

  val onStart = {
    Logger.info("The app is about to start")
    statsActor ! Ping
  }

  lazy val assets: Assets = wire[Assets]
  lazy val prefix: String = "/"
  lazy val router: Router = wire[Routes]
  lazy val applicationController = wire[Application]

  lazy val sunService = wire[SunService]
  lazy val weatherService = wire[WeatherService]

  lazy val statsFilter: Filter = wire[StatsFilter]
  override lazy val httpFilters = Seq(statsFilter)

  lazy val statsActor = actorSystem.actorOf(
    Props(wire[StatsActor]), StatsActor.name
  )
}