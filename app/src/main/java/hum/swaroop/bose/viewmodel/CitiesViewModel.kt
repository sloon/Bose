package hum.swaroop.bose.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import hum.swaroop.bose.BoseApplication
import hum.swaroop.bose.entity.City
import hum.swaroop.bose.entity.Resource
import hum.swaroop.bose.repository.CitiesRepository
import kotlinx.coroutines.experimental.async
import javax.inject.Inject

class CitiesViewModel : ViewModel() {

    @Inject
    lateinit var citiesRepository: CitiesRepository

    private val citiesLiveData = MutableLiveData<Resource<List<City>>>()

    init {
        BoseApplication.component.inject(this)
    }

    fun getCitiesLiveData(): LiveData<Resource<List<City>>> {
        return citiesLiveData
    }

    fun getCities() {
        async {
            val cities = citiesRepository.getCities()
            if (cities != null && cities.isNotEmpty()) {
                citiesLiveData.postValue(Resource(true, null, cities))
            } else {
                citiesLiveData.postValue(Resource(true,
                        "Failed to load cities, try again", null))
            }
        }
    }
}