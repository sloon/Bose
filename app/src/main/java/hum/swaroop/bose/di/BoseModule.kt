package hum.swaroop.bose.di

import android.arch.persistence.room.Room
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import hum.swaroop.bose.api.DARK_SKY_BASE_URL
import hum.swaroop.bose.api.DarkSkyApi
import hum.swaroop.bose.api.OPEN_WEATHER_BASE_URL
import hum.swaroop.bose.api.OpenWeatherApi
import hum.swaroop.bose.dao.LocationDao
import hum.swaroop.bose.database.BoseDatabase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class BoseModule(private val context: Context) {
    @Provides
    @Singleton
    fun provideContext(): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideOpenWeatherApi(): OpenWeatherApi {
        return createRetrofit(OPEN_WEATHER_BASE_URL).create(OpenWeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDarkSkyApi(): DarkSkyApi {
        return createRetrofit(DARK_SKY_BASE_URL).create(DarkSkyApi::class.java)
    }

    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun provideBoseDatabase(context: Context): BoseDatabase {
        return Room.databaseBuilder(context, BoseDatabase::class.java, "bose.db").build()
    }

    @Provides
    fun provideLocationDao(boseDatabase: BoseDatabase): LocationDao = boseDatabase.locationDao()

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    fun provideWorkManager(): WorkManager = WorkManager.getInstance()
}