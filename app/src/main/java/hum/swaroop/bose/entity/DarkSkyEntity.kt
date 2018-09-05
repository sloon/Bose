package hum.swaroop.bose.entity

import com.google.gson.annotations.Expose

data class CurrentData(@Expose val summary: String?,
                       @Expose val temperature: Double?,
                       @Expose val humidity: Double?)

data class DarkSkyData(@Expose val latitude: Double?,
                       @Expose val longitude: Double?,
                       @Expose val currently: CurrentData?)