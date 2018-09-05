package hum.swaroop.bose.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import hum.swaroop.bose.dao.LocationDao
import hum.swaroop.bose.entity.Location

@Database(entities = [Location::class], version = 1)
abstract class BoseDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}