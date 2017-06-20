package controllers

import java.util.concurrent.TimeUnit

import actors.StatsActor
import akka.actor.ActorSystem
import akka.util.Timeout
import model.{CombinedData, SunInfo}
import services.{SunService, WeatherService, AuthService, UserAuthAction}
import org.joda.time.{DateTimeZone, DateTime}
import org.joda.time.format.DateTimeFormat
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.mvc._
import akka.pattern.ask

import play.api.data.Form
import play.api.data.Forms._

import scala.concurrent.ExecutionContext.Implicits.global

case class UserLoginData(username: String, password: String)

class Application(sunService: SunService,
                  weatherService: WeatherService,
                  actorSystem: ActorSystem,
                  authService: AuthService,
                  userAuthAction: UserAuthAction) extends Controller {

  val userDataForm = Form {
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )(UserLoginData.apply)(UserLoginData.unapply)
  }

  def index = Action {
    Ok(views.html.index())
  }

  def login = Action {
    Ok(views.html.login(None))
  }

  def restricted = userAuthAction {
    userAuthRequest =>
      Ok(views.html.restricted(userAuthRequest.user))
  }

  def doLogin = Action(parse.anyContent) {
    implicit request =>
      userDataForm.bindFromRequest.fold(
        formWithErrors => Ok(views.html.login(Some("Wrong data"))),
        userData => {
          val maybeCookie = authService.login(userData.username, userData.password)
          maybeCookie match {
            case Some(cookie) =>
              Redirect("/").withCookies(cookie)
            case None =>
              Ok(views.html.login(Some("Login failed")))
          }
        }
      )
  }

  def data = Action.async {
    val lat = -33.8830
    val lon = 151.2167
    val sunInfoF = sunService.getSunInfo(lat, lon)
    val temperatureF = weatherService.getTemperature(lat, lon)

    implicit val timeout = Timeout(5, TimeUnit.SECONDS)
    val requestF = (actorSystem.actorSelection(StatsActor.path) ?
      StatsActor.GetStats).mapTo[Int]

    for {
      sunInfo <- sunInfoF
      temperature <- temperatureF
      requests <- requestF
    } yield {
      Ok(Json.toJson(CombinedData(sunInfo, temperature, requests)))
    }
  }
}
