package hum.swaroop.bose.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

const val LOCATION_TABLE_NAME = "location"

@Entity(tableName = LOCATION_TABLE_NAME)
data class Location(var latitude: Double, var longitude: Double) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}