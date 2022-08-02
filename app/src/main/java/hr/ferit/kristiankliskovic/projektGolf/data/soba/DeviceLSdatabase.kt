package hr.ferit.kristiankliskovic.projektGolf.data.soba

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hr.ferit.kristiankliskovic.projektGolf.data.DeviceDao
import hr.ferit.kristiankliskovic.projektGolf.model.Device
import hr.ferit.kristiankliskovic.projektGolf.model.LocationSample


@Database(
    entities = [Device::class, LocationSample::class],
    version = 1,
    exportSchema = false
)
abstract class DeviceLSdatabase: RoomDatabase() {

    abstract fun getDeviceDao(): DeviceDao

    companion object {
        private const val databaseName = "ProjektGolfDb"

        @Volatile
        private var INSTANCE: DeviceLSdatabase? = null

        fun getDatabase(context: Context): DeviceLSdatabase {
            if(INSTANCE == null){
                synchronized(this){
                    INSTANCE = buildDatabase(context)
                }
            }
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): DeviceLSdatabase? {
            return Room.databaseBuilder(
                context.applicationContext,
                DeviceLSdatabase::class.java,
                databaseName
            ).allowMainThreadQueries().build()
        }
    }

}