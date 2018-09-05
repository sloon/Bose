package hum.swaroop.bose.repository

import hum.swaroop.bose.api.DarkSkyApi
import hum.swaroop.bose.api.OpenWeatherApi
import hum.swaroop.bose.entity.DarkSkyData
import hum.swaroop.bose.entity.WeatherData
import retrofit2.Response
import javax.inject.Inject

const val OPEN_WEATHER = "open weather"
const val DARK_SKY = "dark sky"
const val PROVIDER = "provider"

class WeatherRepository
@Inject constructor(private val openWeatherApi: OpenWeatherApi, private val darkSkyApi: DarkSkyApi) {

    fun getOpenWeatherDataByCoordinates(latitude: Double, longitude: Double): Response<WeatherData> {
        return openWeatherApi
                .getWeatherDataByCoordinates(latitude, longitude)
                .execute()

    }

    fun getOpenWeatherDataByCityId(id: Long): Response<WeatherData> {
        return openWeatherApi
                .getWeatherDataByCityId(id)
                .execute()
    }

    fun getDarkSkyData(latitude: Double, longitude: Double): Response<DarkSkyData> {
        return darkSkyApi
                .getWeatherDataByCoordinates(latitude.toString()
                        + ","
                        + longitude.toString())
                .execute()
    }
}