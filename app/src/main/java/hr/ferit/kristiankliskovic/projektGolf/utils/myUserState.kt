package hr.ferit.kristiankliskovic.projektGolf.utils

import android.util.Log
import com.google.gson.Gson
import hr.ferit.kristiankliskovic.projektGolf.data.preferencesManager
import hr.ferit.kristiankliskovic.projektGolf.di.DeviceRepositoryFactory
import hr.ferit.kristiankliskovic.projektGolf.model.Device
import hr.ferit.kristiankliskovic.projektGolf.model.user
import hr.ferit.kristiankliskovic.projektGolf.utils.httpAPI.DeviceDataFetcher
import kotlin.math.log

object myUserState {
    var myUser: user? = null
    var myDevice: Device? = null
    private val deviceRepository = DeviceRepositoryFactory.deviceRepository

    fun autoLogin(listener: genericListener) {
        val myUser = preferencesManager.getUser()
        Log.i("usersList", Gson().toJson(myUser))
        if (myUser != null) {
            var found = false
            for (user in firebaseComm.users) {
                if (
                    user.username == myUser.username &&
                    user.password == myUser.password
                ) {
                    found = true
                    Log.i("userInBase", Gson().toJson(myUser))
                    login(user, false, listener)
                }
            }
            if(!found){
                logout()
                listener.callEnded()
            }
        }
        else{
            listener.callEnded()
        }
    }

    fun changeDevice(deviceName: String) {
        Log.i("changeDevice", Gson().toJson(myUser))
        for (devices in myUser!!.devices) {
            if (devices.name == deviceName) {
                preferencesManager.saveCurrDeviceName(deviceName)
                myDevice = devices
            }
        }
    }

    fun login(user: user, resetSettings: Boolean, listener: genericListener?) {
        Log.i("enteringLogin", Gson().toJson(user))
        if (user!!.devices == null) {
            user!!.devices = arrayListOf()
        }
        preferencesManager.setUser(user)
        myUser = user

        val savedDevName = preferencesManager.getCurrDeviceName()
        if (myUser!!.devices != null) {
            for (dev in myUser!!.devices) {
                if (dev.name == savedDevName) {
                    myDevice = dev
                }
            }
            if (myDevice == null && user.devices.size > 0) {
                myDevice = myUser!!.devices[0]
                preferencesManager.saveCurrDeviceName(myDevice!!.name)
            }
            if(myDevice == null){
                preferencesManager.saveCurrDeviceName("")
            }
        }
        Log.i("loginX", "prije resetSetting grananja")
        if (resetSettings) {
            preferencesManager.setChoice(2)
            preferencesManager.setHistory("1 day")
            preferencesManager.saveTimestamp(1, "")
            preferencesManager.saveTimestamp(2, "")

            val numberofDevices = myUser!!.devices.size
            if (numberofDevices == 0) listener?.callEnded()
            var devicesLoaded = 0
            val addingListener: genericListener =
                object : genericListener {
                    override fun callEnded() {
                        devicesLoaded++
                        if (devicesLoaded == numberofDevices) {
                            listener!!.callEnded()
                        }
                    }
                }
            for (device in myUser!!.devices) {
                DeviceDataFetcher().fetchAllandSave(device.channelId,
                    device.readAPIkey,
                    addingListener)
                deviceRepository.save(device)
            }

        } else {
            val listaNovihUredaja: ArrayList<Device> = arrayListOf()
            var devicesDownloaded = 0


            for (device in myUser!!.devices) {
                var deviceExists = false
                for (device2 in deviceRepository.getAllDevices()) {
                    if (device.name == device2.name) {
                        deviceExists = true
                    }
                }
                if (!deviceExists) {
                    Log.i("loginX", "uredajNepostojiOffline" + device.channelId)
                    listaNovihUredaja.add(device)
                    deviceRepository.save(device)
                }
            }
            val addingListener: genericListener =
                object : genericListener {
                    override fun callEnded() {
                        devicesDownloaded++
                        if (devicesDownloaded == listaNovihUredaja.size) {
                            Log.i("loginX", "SKINUT UREDAJ")
                            listener!!.callEnded()
                        }
                    }
                }

            for (device in listaNovihUredaja) {
                Log.i("loginX", "novi uredaj: "+Gson().toJson(device))
                DeviceDataFetcher().fetchAllandSave(device.channelId,
                    device.readAPIkey,
                    addingListener)
            }


            for (device in deviceRepository.getAllDevices()) {
                Log.i("loginX", "ROOM " + device.name )
                var validDevice = false
                for (device2 in myUser!!.devices) {
                    Log.i("loginX", "FIREBASE " + device2.name )
                    if (device.name == device2.name) {
                        validDevice = true
                    }
                }
                if (!validDevice) {
                    deviceRepository.deleteLSfrom(device)
                    deviceRepository.delete(device)
                }
            }
            listener?.callEnded()
        }
    }

    fun logout() {
        preferencesManager.removeUser()
        preferencesManager.saveCurrDeviceName("")
        preferencesManager.setChoice(2)
        preferencesManager.setHistory("1 day")
        preferencesManager.saveTimestamp(1, "")
        preferencesManager.saveTimestamp(2, "")
        while (deviceRepository.getAllDevices().isNotEmpty()) {
            deviceRepository.deleteLSfrom(deviceRepository.getAllDevices()[0])
            deviceRepository.delete(deviceRepository.getAllDevices()[0])
        }
        myDevice = null
        myUser = null
    }

    fun addDevice(device: Device) {
        if (myUser!!.devices == null) {
            myUser!!.devices = arrayListOf()
        }
        myUser!!.devices!!.add(device)
        preferencesManager.setUser(myUser!!)
        myDevice = device
        preferencesManager.saveCurrDeviceName(device.name)
        firebaseComm.changeUser(myUser!!)
    }

    fun deleteUser() {
        firebaseComm.deleteUser(myUser!!)
        logout()
    }

    fun deleteDevice(deviceName: String){
        for(device in myUser!!.devices){
            if(device.name == deviceName){
                myUser!!.devices.remove(device)
                deviceRepository.deleteLSfrom(device)
                deviceRepository.delete(device)
            }
        }
        if(myDevice?.name == deviceName){
            myDevice = null
        }
        firebaseComm.changeUser(myUser!!)
    }

    fun changePassword(newPass: String){
        myUser!!.password = newPass
        preferencesManager.setUser(myUser!!)
        firebaseComm.changeUser(myUser!!)
    }
}