package hum.swaroop.bose

import hum.swaroop.bose.api.DARK_SKY_BASE_URL
import hum.swaroop.bose.api.DarkSkyApi
import hum.swaroop.bose.api.OPEN_WEATHER_BASE_URL
import hum.swaroop.bose.api.OpenWeatherApi
import hum.swaroop.bose.entity.DarkSkyData
import hum.swaroop.bose.entity.WeatherData
import hum.swaroop.bose.repository.WeatherRepository
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRepositoryTest {

    private lateinit var openWeatherApi: OpenWeatherApi
    private lateinit var darkSkyApi: DarkSkyApi
    private lateinit var weatherRepository: WeatherRepository

    @Before
    fun create() {
        openWeatherApi = Retrofit.Builder()
                .baseUrl(OPEN_WEATHER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenWeatherApi::class.java)

        darkSkyApi = Retrofit.Builder()
                .baseUrl(DARK_SKY_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DarkSkyApi::class.java)
        weatherRepository = WeatherRepository(openWeatherApi, darkSkyApi)
    }

    @Test
    fun testOpenWeatherDataValid() {
        val response = weatherRepository.getOpenWeatherDataByCityId(519188)
        assertTrue(response.isSuccessful)
        assertTrue(response.body() is WeatherData)
        assertTrue(response.body()?.name.equals("Novinki"))
    }

    @Test
    fun testDarkSkyDataValid() {
        val response = weatherRepository.getDarkSkyData(55.683334, 37.666668)
        assertTrue(response.isSuccessful)
        assertTrue(response.body() is DarkSkyData)
    }
}
