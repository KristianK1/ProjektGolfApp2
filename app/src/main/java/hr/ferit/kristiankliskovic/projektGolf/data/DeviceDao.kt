package hr.ferit.kristiankliskovic.projektGolf.data

import androidx.room.*
import hr.ferit.kristiankliskovic.projektGolf.model.Device
import hr.ferit.kristiankliskovic.projektGolf.model.LocationSample

@Dao
interface DeviceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(device: Device)

    @Delete
    fun delete(device: Device)

    @Query("SELECT * from devices")
    fun getAllDevices(): List<Device>




    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocationSamples(Ls: LocationSample)

    @Query("DELETE FROM locationSamples WHERE channelId = :chId AND ReadAPIkey = :api")
    fun deleteLSfrom(chId: String, api: String)

    @Query("SELECT * FROM locationSamples WHERE channelId = :chId AND ReadAPIkey = :api")
    fun getLSfrom(chId: String, api: String): List<LocationSample>

    /*fun save(device: Device)
    fun delete(device: Device)
    fun getAllDevices(): List<Device>

    fun insertLocationSamples(Ls_list: List<LocationSample>)
    fun delete_LS_from(device: Device)
    fun get_LS_from(device: Device): List<LocationSample>*/
}