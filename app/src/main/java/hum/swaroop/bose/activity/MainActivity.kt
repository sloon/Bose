package hum.swaroop.bose.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import hum.swaroop.bose.R
import hum.swaroop.bose.adapter.CITY_ID
import hum.swaroop.bose.entity.DarkSkyData
import hum.swaroop.bose.entity.WeatherData
import hum.swaroop.bose.getCityCountryFromLatLng
import hum.swaroop.bose.repository.DARK_SKY
import hum.swaroop.bose.repository.OPEN_WEATHER
import hum.swaroop.bose.toFahrenheit
import hum.swaroop.bose.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val CITY_ID_REQUEST = 1
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        checkPermissionAndSaveLocation()

        viewSavedLocations.setOnClickListener {

        }
        changeProvider.setOnClickListener {
            var selection = 0
            when (viewModel.getProvider()) {
                DARK_SKY -> {
                    selection = 1
                }
            }
            AlertDialog.Builder(this)
                    .setTitle(R.string.change_provider)
                    .setSingleChoiceItems(R.array.providers, selection) { d, i ->
                        when (i) {
                            0 -> {
                                viewModel.changeProvider(OPEN_WEATHER)
                                reset()
                                d.dismiss()
                            }
                            1 -> {
                                viewModel.changeProvider(DARK_SKY)
                                reset()
                                d.dismiss()
                            }
                        }
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .create().show()
        }


        viewModel.getOpenWeatherLiveData().observe(this, Observer { resource ->
            resource?.let {
                if (it.success) {
                    populateViewsWithOpenWeatherData(it.data)
                } else if (it.errorMessage != null) {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.getDarkSkyLiveData().observe(this, Observer { resource ->
            resource?.let {
                if (it.success) {
                    populateViewsWithDarkSkyData(it.data)
                } else if (it.errorMessage != null) {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.getLocationLiveData().observe(this, Observer { resource ->
            resource?.let { task ->
                if (task.success && task.data != null) {
                    viewModel.saveCityId(0L)
                    viewModel.saveLatLng(0f, 0f)
                    when(viewModel.getProvider()) {
                        DARK_SKY -> {
                            viewModel.getDarkSkyDataByCoordinates(task.data.latitude,
                                    task.data.longitude)
                        }
                        else -> {
                            viewModel.getOpenWeatherDataByCoordinates(task.data.latitude,
                                    task.data.longitude)
                        }
                    }
                } else if (task.errorMessage != null) {
                    Toast.makeText(this, task.errorMessage, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, R.string.emulator_error, Toast.LENGTH_SHORT).show()
                }
            }
        })

        reset()

        viewModel.getWeatherStatusLiveData().observe(this, Observer { statusList ->
            if (statusList == null || statusList.isEmpty()) {
                viewModel.resetWeatherWork()
            }
        })
    }

    override fun onStop() {
        checkPermissionAndSaveLocation()
        super.onStop()
    }

    private fun reset() {
        when (viewModel.getProvider()) {
            DARK_SKY -> {
                val l = viewModel.getLatLng()
                if (l[0] > 0 && l[1] > 0) {
                    viewModel.getDarkSkyDataByCoordinates(l[0].toDouble(), l[1].toDouble())
                } else {
                    checkLocationPermissionAndGetWeather()
                }
            }
            else -> {
                val id = viewModel.getSavedCityId()
                if (id > 0) {
                    viewModel.getOpenWeatherDataById(id)
                } else {
                    checkLocationPermissionAndGetWeather()
                }
            }
        }
    }

    private fun checkPermissionAndSaveLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            viewModel.startWorkToSaveLocation()
        }
    }

    private fun checkLocationPermissionAndGetWeather() {
        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
        } else {
            viewModel.getCurrentLocation(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CITY_ID_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.getLongExtra(CITY_ID, 0)?.let { id ->
                        if (id > 0) {
                            viewModel.saveCityId(id)
                            viewModel.getOpenWeatherDataById(id)
                        }
                    }
                    val lat = data?.getDoubleExtra("lat", 0.0)
                    val lng = data?.getDoubleExtra("lng", 0.0)

                    if (lat != null && lng != null) {
                        viewModel.saveLatLng(lat.toFloat(), lng.toFloat())
                    }
                }
            }
        }
    }

    private fun populateViewsWithOpenWeatherData(weatherData: WeatherData?) {
        weatherData?.let { data ->
            data.main?.temp?.let {
                temperature.text = getString(R.string.temperature, it.toFahrenheit())
            }
            name.text = getString(R.string.city_name, data.name, data.sys?.country)
            humidity.text = getString(R.string.humidity, data.main?.humidity)
        }
    }

    private fun populateViewsWithDarkSkyData(darkSkyData: DarkSkyData?) {
        darkSkyData?.let { data ->
            data.currently.let {
                temperature.text = getString(R.string.temperature, it?.temperature)
                humidity.text = getString(R.string.humidity, it?.humidity)
            }
            if (data.longitude != null && data.latitude != null) {
                name.text = getCityCountryFromLatLng(this, data.latitude, data.longitude)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_select_city -> {
                startActivityForResult(Intent(this, CitiesActivity::class.java),
                        CITY_ID_REQUEST)
                return true
            }
            R.id.action_select_current_location -> {
                checkLocationPermissionAndGetWeather()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.getCurrentLocation(this)
            } else {
                startActivityForResult(Intent(this, CitiesActivity::class.java),
                        CITY_ID_REQUEST)
            }
        }
    }
}
