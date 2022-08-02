package hr.ferit.kristiankliskovic.projektGolf.data

import hr.ferit.kristiankliskovic.projektGolf.model.Device
import hr.ferit.kristiankliskovic.projektGolf.model.LocationSample

class DeviceRepositoryImpl(val deviceDao: DeviceDao): DeviceRepository {
    override fun delete(device: Device) = deviceDao.delete(device)
    override fun getAllDevices(): List<Device> = deviceDao.getAllDevices()
    override fun save(device: Device) = deviceDao.save(device)


    override fun deleteLSfrom(device: Device) = deviceDao.deleteLSfrom(device.channelId, device.ReadAPIkey)
    override fun insertLocationSamples(Ls: LocationSample) = deviceDao.insertLocationSamples(Ls)
    override fun getLSfrom(device: Device): List<LocationSample> = deviceDao.getLSfrom(device.channelId, device.ReadAPIkey)
}