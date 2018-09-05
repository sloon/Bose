package hum.swaroop.bose.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import hum.swaroop.bose.entity.LOCATION_TABLE_NAME
import hum.swaroop.bose.entity.Location

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(profile: Location)

    @Query("SELECT * FROM $LOCATION_TABLE_NAME")
    fun getSavedLocations(): List<Location>
}