package hum.swaroop.bose.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hum.swaroop.bose.entity.City
import javax.inject.Inject

class CitiesRepository @Inject constructor() {

    fun getCities(): List<City>? {
        val dataJson = this::class.java.getResource("/assets/city_list.json")?.readText()
        var list: List<City>? = null
        dataJson?.let {
            list = Gson().fromJson<List<City>>(it)
        }
        return list
    }

    private inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)
}