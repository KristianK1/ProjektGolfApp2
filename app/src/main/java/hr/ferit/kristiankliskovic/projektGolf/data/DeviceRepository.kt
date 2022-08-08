package hr.ferit.kristiankliskovic.projektGolf.data

import hr.ferit.kristiankliskovic.projektGolf.model.Device
import hr.ferit.kristiankliskovic.projektGolf.model.LocationSample

interface DeviceRepository {
    fun save(device: Device)
    fun delete(device: Device)
    fun getAllDevices(): List<Device>
    fun updatedLastUpdated(timeStamp: String, device: Device)

    fun insertLocationSamples(Ls: LocationSample)
    fun deleteLSfrom(device: Device)
    fun getLSfrom(device: Device, date1: String, date2: String): List<LocationSample>
    fun getallLS(): List<LocationSample>
    fun insertMultipleinsertsLocationSamples(LsList: MutableList<LocationSample>)
    fun getLastLSfrom(chId: String, api: String, upToDate: String): List<LocationSample>
}