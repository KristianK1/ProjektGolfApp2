package hr.ferit.kristiankliskovic.projektGolf.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locationSamples",
primaryKeys = ["channelId", "ReadAPIkey", "entry_id", "created_at", "longitude", "latitude", "inLineIndex"])
data class LocationSample(
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
    val marker: Boolean,

    @ColumnInfo(name = "inLineIndex")
    val inLineIndex: Int
){}
