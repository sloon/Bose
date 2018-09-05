package hum.swaroop.bose.worker

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v4.app.NotificationCompat
import androidx.work.Worker
import hum.swaroop.bose.BoseApplication
import hum.swaroop.bose.R
import hum.swaroop.bose.activity.MainActivity
import hum.swaroop.bose.adapter.CITY_ID
import hum.swaroop.bose.entity.WeatherData
import hum.swaroop.bose.repository.LocationRepository
import hum.swaroop.bose.repository.WeatherRepository
import hum.swaroop.bose.toFahrenheit
import retrofit2.Response
import javax.inject.Inject

class WeatherWorker : Worker() {

    @Inject
    lateinit var locationRepository: LocationRepository

    @Inject
    lateinit var weatherRepository: WeatherRepository

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    init {
        BoseApplication.component.inject(this)
    }

    override fun doWork(): Result {
        val id = sharedPreferences.getLong(CITY_ID, 0L)
        var response: Response<WeatherData>? = null
        if (id > 0) {
            response = weatherRepository.getOpenWeatherDataByCityId(id)
        } else {
            val location = locationRepository.getLocation()
            location.addOnSuccessListener {
                response = weatherRepository
                        .getOpenWeatherDataByCoordinates(it.latitude, it.longitude)
            }
        }

        response?.let {
            val weatherData = it.body()
            if (!it.isSuccessful || weatherData == null) return@let
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(applicationContext,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            var notificationBuilder = NotificationCompat.Builder(applicationContext,
                    applicationContext.packageName)
            notificationBuilder = notificationBuilder
                    .setSmallIcon(android.R.drawable.btn_star)
                    .setContentTitle(applicationContext.getString(R.string.city_name,
                            it.body()?.name, weatherData.sys?.country))
                    .setContentText(applicationContext.getString(R.string.notification_text,
                            weatherData.main?.temp?.toFahrenheit(),
                            weatherData.weather?.get(0)?.description))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)

            val notificationManager = applicationContext
                    .getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            notificationManager?.notify(1, notificationBuilder.build())
        }
        return Result.SUCCESS
    }
}