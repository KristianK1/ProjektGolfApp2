package hr.ferit.kristiankliskovic.projektGolf.di

import hr.ferit.kristiankliskovic.projektGolf.data.DeviceRepositoryImpl
import hr.ferit.kristiankliskovic.projektGolf.data.soba.DeviceLSdatabase
import hr.ferit.kristiankliskovic.projektGolf.mainSomething


object DeviceRepositoryFactory {
    val roomDb = DeviceLSdatabase.getDatabase(mainSomething.application)
    val deviceRepository = DeviceRepositoryImpl(roomDb.getDeviceDao())
}