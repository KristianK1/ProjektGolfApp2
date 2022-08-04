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

    @ColumnInfo(name="entry_id")
    var entry_id: Int,

    @ColumnInfo(name="created_at")
    var created_at: String,

    @ColumnInfo(name = "longitude")
    val longitude: Double,

    @ColumnInfo(name="latitude")
    val latitude: Double,

    @ColumnInfo(name="speed")
    val speed: Double,

    @ColumnInfo(name="marker")
    val marker: Boolean
){}
