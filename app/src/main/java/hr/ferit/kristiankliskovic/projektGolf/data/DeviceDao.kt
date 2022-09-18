package hr.ferit.kristiankliskovic.projektGolf.data


import androidx.room.*
import hr.ferit.kristiankliskovic.projektGolf.model.Device
import hr.ferit.kristiankliskovic.projektGolf.model.LocationSample


@Dao
interface DeviceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(device: Device)

    @Query("DELETE FROM devices WHERE channelId = :chId AND ReadAPIkey = :api")
    fun delete(chId: String, api: String)

    @Query("SELECT * from devices")
    fun getAllDevices(): List<Device>

    @Query("UPDATE devices SET lastRefreshed = :timeStamp WHERE channelId=:chId AND ReadAPIkey=:ReadAPIkey")
    fun updatedLastUpdated(timeStamp: String, chId: String, ReadAPIkey: String)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocationSamples(Ls: LocationSample)

    @Query("DELETE FROM locationSamples WHERE channelId = :chId AND ReadAPIkey = :api")
    fun deleteLSfrom(chId: String, api: String)

    @Query("SELECT * FROM locationSamples WHERE channelId = :chId AND ReadAPIkey = :api AND created_at BETWEEN :date1 AND :date2 ORDER BY created_at ASC, inLineIndex ASC")
    fun getLSfrom(chId: String, api: String, date1: String, date2: String): List<LocationSample>


    @Query("SELECT * FROM locationSamples")
    fun getallLS(): List<LocationSample>

    @Transaction
    fun insertMultipleinsertsLocationSamples(LsList: MutableList<LocationSample>) {
        for(sample in LsList){
            insertLocationSamples(sample)
        }
    }

    @Query("SELECT * FROM locationSamples WHERE channelId = :chId AND ReadAPIkey = :api AND created_at BETWEEN 0 AND :upToDate ORDER BY created_at DESC, inLineIndex DESC LIMIT 1")
    fun getLastLSfrom(chId: String, api: String, upToDate: String): List<LocationSample>
}