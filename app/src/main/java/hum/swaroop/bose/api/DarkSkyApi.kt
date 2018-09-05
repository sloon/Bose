package hum.swaroop.bose.api

import hum.swaroop.bose.entity.DarkSkyData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

const val DARK_SKY_BASE_URL = "https://api.darksky.net"

interface DarkSkyApi {

    @GET("forecast/3d057012d465fa951052912c0c7e24e5/{coordinates}")
    fun getWeatherDataByCoordinates(@Path("coordinates") latcommalng: String): Call<DarkSkyData>
}