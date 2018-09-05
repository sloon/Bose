package hum.swaroop.bose.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import androidx.work.*
import hum.swaroop.bose.BoseApplication
import hum.swaroop.bose.adapter.CITY_ID
import hum.swaroop.bose.dao.LocationDao
import hum.swaroop.bose.entity.DarkSkyData
import hum.swaroop.bose.entity.Resource
import hum.swaroop.bose.entity.WeatherData
import hum.swaroop.bose.repository.LocationRepository
import hum.swaroop.bose.repository.OPEN_WEATHER
import hum.swaroop.bose.repository.PROVIDER
import hum.swaroop.bose.repository.WeatherRepository
import hum.swaroop.bose.worker.SaveLocationWorker
import hum.swaroop.bose.worker.WeatherWorker
import kotlinx.coroutines.experimental.async
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject
    lateinit var weatherRepository: WeatherRepository

    @Inject
    lateinit var locationRepository: LocationRepository

    @Inject
    lateinit var locationDao: LocationDao

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var workManager: WorkManager

    private val openWeatherLiveData = MutableLiveData<Resource<WeatherData>>()
    private val darkSkyLiveData = MutableLiveData<Resource<DarkSkyData>>()
    private val locationLiveData = MutableLiveData<Resource<Location>>()

    init {
        BoseApplication.component.inject(this)
    }

    fun getOpenWeatherLiveData(): LiveData<Resource<WeatherData>> {
        return openWeatherLiveData
    }

    fun getDarkSkyLiveData(): LiveData<Resource<DarkSkyData>> {
        return darkSkyLiveData
    }

    fun getLocationLiveData(): LiveData<Resource<Location>> {
        return locationLiveData
    }

    fun getOpenWeatherDataByCoordinates(latitude: Double, longitude: Double) {
        async {
            val response = weatherRepository.getOpenWeatherDataByCoordinates(latitude, longitude)
            if (response.isSuccessful && response.body() != null) {
                openWeatherLiveData.postValue(Resource(true, null, response.body()))
            } else {
                openWeatherLiveData.postValue(Resource(false, response.errorBody()?.string(), null))
            }
        }
    }

    fun getDarkSkyDataByCoordinates(latitude: Double, longitude: Double) {
        async {
            val response = weatherRepository.getDarkSkyData(latitude, longitude)
            if (response.isSuccessful && response.body() != null) {
                darkSkyLiveData.postValue(Resource(true, null, response.body()))
            } else {
                darkSkyLiveData.postValue(Resource(false, response.errorBody()?.string(), null))
            }
        }
    }

    fun getOpenWeatherDataById(id: Long) {
        async {
            val response = weatherRepository.getOpenWeatherDataByCityId(id)
            if (response.isSuccessful && response.body() != null) {
                openWeatherLiveData.postValue(Resource(true, null, response.body()))
            } else {
                openWeatherLiveData.postValue(Resource(false, response.errorBody()?.string(), null))
            }
        }
    }

    fun getCurrentLocation(context: Context) {
        async {
            val location = locationRepository.getLocation()
            location.addOnSuccessListener {
                locationLiveData.postValue(Resource(true, null, it))
            }
            location.addOnFailureListener {
                locationLiveData.postValue(Resource(false, it.message, null))
            }
        }
    }

//    fun saveLocationToDb() {
//        async {
//            locationRepository.getLocation().addOnSuccessListener {
//                locationDao.insertLocation(hum.swaroop.bose.entity.Location(it.latitude, it.longitude))
//            }
//        }
//    }

    fun getSavedCityId(): Long {
        return sharedPreferences.getLong(CITY_ID, 0L)
    }

    fun saveCityId(id: Long) {
        sharedPreferences.edit().putLong(CITY_ID, id).apply()
    }

    fun getWeatherStatusLiveData(): LiveData<List<WorkStatus>> {
        return workManager.getStatusesByTag("Weather")
    }

    fun resetWeatherWork() {
        val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        val weatherWorker = PeriodicWorkRequestBuilder<WeatherWorker>(15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .addTag("Weather")
                .build()
        workManager.cancelAllWorkByTag("Weather")
        workManager.enqueue(weatherWorker)
    }

    fun startWorkToSaveLocation() {
        workManager.enqueue(OneTimeWorkRequestBuilder<SaveLocationWorker>().build())
    }

    fun changeProvider(provider: String) {
        sharedPreferences.edit().putString(PROVIDER, provider).apply()
    }

    fun getProvider(): String? {
        return sharedPreferences.getString(PROVIDER, OPEN_WEATHER)
    }

    fun getLatLng(): List<Float> {
        return listOf(sharedPreferences.getFloat("lat", 0f), sharedPreferences.getFloat("lat", 0f))
    }

    fun saveLatLng(lat: Float, lng: Float) {
        sharedPreferences.edit().putFloat("lat", lat).putFloat("lng", lng).apply()
    }
}