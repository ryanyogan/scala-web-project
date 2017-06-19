package services

import model.SunInfo
import org.joda.time.{DateTimeZone, DateTime}
import org.joda.time.format.DateTimeFormat
import play.api.libs.ws.{WSClient, WS}

import scala.concurrent.Future
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global

class SunService(wsClient: WSClient) {
  def getSunInfo(lat: Double, lon: Double): Future[SunInfo] = {
    val responseF = wsClient.url("http://api.sunrise-sunset.org/json?" +
      s"lat=$lat&lng=$lon&formatted=0").get()

    responseF.map { response =>
      val json = response.json
      val sunriseTimeStr = (json \ "results" \ "sunrise").as[String]
      val sunsetTimeStr = (json \ "results" \ "sunset").as[String]
      val sunriseTime = DateTime.parse(sunriseTimeStr)
      val sunsetTime = DateTime.parse(sunsetTimeStr)
      val formatter = DateTimeFormat.forPattern("HH:mm:ss").
        withZone(DateTimeZone.forID("Australia/Sydney"))
      val sunInfo = SunInfo(formatter.print(sunriseTime),
        formatter.print(sunsetTime))

      SunInfo(formatter.print(sunriseTime), formatter.print(sunsetTime))
    }
  }
}