package hum.swaroop.bose.worker

import androidx.work.Worker
import hum.swaroop.bose.BoseApplication
import hum.swaroop.bose.dao.LocationDao
import hum.swaroop.bose.repository.LocationRepository
import javax.inject.Inject

class SaveLocationWorker : Worker() {

    @Inject
    lateinit var locationRepository: LocationRepository
    @Inject
    lateinit var locationDao: LocationDao


    init {
        BoseApplication.component.inject(this)
    }

    override fun doWork(): Result {
        locationRepository.getLocation().addOnSuccessListener {
            locationDao.insertLocation(hum.swaroop.bose.entity.Location(it.latitude, it.longitude))
        }
        return Result.SUCCESS
    }

}