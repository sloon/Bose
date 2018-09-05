package hum.swaroop.bose.di

import dagger.Component
import hum.swaroop.bose.activity.CitiesActivity
import hum.swaroop.bose.activity.MainActivity
import hum.swaroop.bose.viewmodel.CitiesViewModel
import hum.swaroop.bose.viewmodel.MainViewModel
import hum.swaroop.bose.worker.SaveLocationWorker
import hum.swaroop.bose.worker.WeatherWorker
import javax.inject.Singleton

@Singleton
@Component(modules = [BoseModule::class])
interface BoseComponent {

    fun inject(activity: MainActivity)
    fun inject(activity: CitiesActivity)

    fun inject(viewModel: MainViewModel)
    fun inject(viewModel: CitiesViewModel)

    fun inject(worker: WeatherWorker)
    fun inject(worker: SaveLocationWorker)
}