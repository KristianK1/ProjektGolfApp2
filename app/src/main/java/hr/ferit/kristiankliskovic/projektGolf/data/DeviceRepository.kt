package hr.ferit.kristiankliskovic.projektGolf.data

import hr.ferit.kristiankliskovic.projektGolf.model.Device
import hr.ferit.kristiankliskovic.projektGolf.model.LocationSample

interface DeviceRepository {
    fun save(device: Device)
    fun delete(device: Device)
    fun getAllDevices(): List<Device>

    fun insertLocationSamples(Ls: LocationSample)
    fun deleteLSfrom(device: Device)
    fun getLSfrom(device: Device): List<LocationSample>
}