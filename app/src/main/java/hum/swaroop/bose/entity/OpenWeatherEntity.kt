package hum.swaroop.bose.entity

import com.google.gson.annotations.Expose

data class Coordinates(@Expose val lon: Double?,
                       @Expose val lat: Double?)

data class Weather(@Expose val id: Long?,
                   @Expose val main: String?,
                   @Expose val description: String?)

data class Main(@Expose val temp: Double?,
                @Expose val humidity: Double?)

data class Sys(@Expose val country: String?)

data class WeatherData(@Expose val visibility: Long?,
                       @Expose val name: String?,
                       @Expose val main: Main?,
                       @Expose val sys: Sys?,
                       @Expose val coord: Coordinates?,
                       @Expose val weather: List<Weather>?)

data class City(@Expose val id: Long?,
                @Expose val name: String?,
                @Expose val country: String?,
                @Expose val coord: Coordinates?)
