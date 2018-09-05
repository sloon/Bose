package hum.swaroop.bose.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import javax.inject.Inject

class LocationRepository @Inject constructor(private val context: Context) {

    @SuppressLint("MissingPermission")
    fun getLocation(): Task<Location> {
        return LocationServices.getFusedLocationProviderClient(context).lastLocation
    }
}