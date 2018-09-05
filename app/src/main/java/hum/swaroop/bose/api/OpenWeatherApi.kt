package hum.swaroop.bose.api

import hum.swaroop.bose.entity.WeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val OPEN_WEATHER_BASE_URL = "https://api.openweathermap.org/"

interface OpenWeatherApi {

    @GET("data/2.5/weather")
    fun getWeatherDataByCoordinates(@Query("lat") latitude: Double,
                                    @Query("lon") longitude: Double,
                                    @Query("APPID") appid: String = "1169d72dc013ef754925d3e66e9a7ed4"): Call<WeatherData>

    @GET("data/2.5/weather")
    fun getWeatherDataByCityId(@Query("id") id: Long,
                               @Query("APPID") appid: String = "1169d72dc013ef754925d3e66e9a7ed4"): Call<WeatherData>
}