package hum.swaroop.bose

import android.content.Context
import android.location.Geocoder
import java.util.*


fun Double.toFahrenheit(): Double {
    return this * 9f / 5f - 459.67
}

fun getCityCountryFromLatLng(context: Context, lat: Double, lng: Double): String {
    val geocoder = Geocoder(context, Locale.getDefault())
    val addresses = geocoder.getFromLocation(lat, lng, 1)


    val city = addresses[0].getLocality()

    val country = addresses[0].getCountryName()
    return "$city, $country"
}