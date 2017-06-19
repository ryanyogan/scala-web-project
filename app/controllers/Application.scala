package controllers

import play.api._
import play.api.mvc._
import javax.inject._
import org.joda.time.{DateTime, DateTimeZone}
import org.joda.time.format.DateTimeFormat
import play.api.libs.ws._
import play.api.libs.json._
import play.api.Play.current
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import services.{SunService, WeatherService}
import model.SunInfo
import java.util.concurrent.TimeUnit
import akka.util.Timeout
import akka.pattern.ask
import akka.actor.ActorSystem
import actors.StatsActor

class Application(sunService: SunService,
                  weatherService: WeatherService,
                  actorSystem: ActorSystem) extends Controller {

  def index = Action.async {

    implicit val timeout = Timeout(5, TimeUnit.SECONDS)
    val requestF = (actorSystem.actorSelection(StatsActor.path) ?
      StatsActor.GetStats).mapTo[Int]

    val lat = -33.8830
    val lon = 151.2167
    val sunInfoF = sunService.getSunInfo(lat, lon)
    val temperatureF = weatherService.getTemperature(lat, lon)

    for {
      sunInfo <- sunInfoF
      temperature <- temperatureF
      requests <- requestF
    } yield {
      Ok(views.html.index(sunInfo, temperature, requests))
    }
  }
}
