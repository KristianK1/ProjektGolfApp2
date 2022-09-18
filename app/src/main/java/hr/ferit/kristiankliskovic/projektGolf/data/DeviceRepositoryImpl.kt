package hr.ferit.kristiankliskovic.projektGolf.data

import hr.ferit.kristiankliskovic.projektGolf.model.Device
import hr.ferit.kristiankliskovic.projektGolf.model.LocationSample

class DeviceRepositoryImpl(val deviceDao: DeviceDao): DeviceRepository {
    override fun delete(device: Device) = deviceDao.delete(device)
    override fun getAllDevices(): List<Device> = deviceDao.getAllDevices()
    override fun save(device: Device) = deviceDao.save(device)
    override fun updatedLastUpdated(timeStamp: String, device: Device) = deviceDao.updatedLastUpdated(timeStamp, device.channelId, device.readAPIkey)

    override fun deleteLSfrom(device: Device) = deviceDao.deleteLSfrom(device.channelId, device.readAPIkey)
    override fun insertLocationSamples(Ls: LocationSample) = deviceDao.insertLocationSamples(Ls)
    override fun getLSfrom(device: Device, date1: String, date2: String): List<LocationSample> = deviceDao.getLSfrom(device.channelId, device.readAPIkey, date1, date2)
    override fun getallLS(): List<LocationSample> = deviceDao.getallLS()
    override fun insertMultipleinsertsLocationSamples(LsList: MutableList<LocationSample>) = deviceDao.insertMultipleinsertsLocationSamples(LsList)
    override fun getLastLSfrom(chId: String, api: String, upToDate: String): List<LocationSample>  = deviceDao.getLastLSfrom(chId,api, upToDate)
}