package services

import play.api.libs.ws.{WSClient, WS}

import scala.concurrent.Future

import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global

class WeatherService(wsClient: WSClient) {
  def getTemperature(lat: Double, lon: Double): Future[Double] = {
    val weatherResponseF = wsClient.url("http://api.openweathermap.org/data/2.5/" +
      s"weather?lat=$lat&lon=$lon&units=metric" +
      "&APPID=f17f1d84b9b8431e698a6c71a3f173c1").get()
    
    weatherResponseF.map { weatherResponse =>
      val weatherJson = weatherResponse.json
      val temperature = (weatherJson \ "main" \ "temp").as[Double]
      temperature
    }
  }
}