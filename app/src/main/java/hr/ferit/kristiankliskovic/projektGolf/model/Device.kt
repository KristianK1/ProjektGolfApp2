package hr.ferit.kristiankliskovic.projektGolf.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
data class Device(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "channelId")
    var channelId: String,

    @ColumnInfo(name="ReadAPIkey")
    var ReadAPIkey: String,

    @ColumnInfo(name="BT_MAC_address")
    var BT_MAC_address: String,

    @ColumnInfo(name="lastRefreshed")
    var lastRefreshed: String,
){}
