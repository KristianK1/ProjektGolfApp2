package hr.ferit.kristiankliskovic.projektGolf.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locationSamples")
data class LocationSample(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @ColumnInfo(name="channelId")
    var channelId: String,

    @ColumnInfo(name="ReadAPIkey")
    var ReadAPIkey: String,

    @ColumnInfo(name = "longitude")
    val longitude: Double,

    @ColumnInfo(name="latitude")
    val latitude: Double,

    @ColumnInfo(name="speed")
    val speed: Double
){}
