package hr.ferit.kristiankliskovic.projektGolf.utils.httpAPI

import android.util.Log
import com.google.gson.Gson
import hr.ferit.kristiankliskovic.projektGolf.di.DeviceRepositoryFactory
import hr.ferit.kristiankliskovic.projektGolf.model.Device
import hr.ferit.kristiankliskovic.projektGolf.model.LocationSample
import hr.ferit.kristiankliskovic.projektGolf.model.TSmainClass
import hr.ferit.kristiankliskovic.projektGolf.utils.genericListener
import hr.ferit.kristiankliskovic.projektGolf.utils.*
import java.util.*
import kotlin.math.absoluteValue

class DeviceDataFetcher {
    val gson = Gson()
    val deviceRepository = DeviceRepositoryFactory.deviceRepository


    fun fetchInterval(
        chId: String,
        APIkey: String,
        timeStampStart: String,
        timeStampEnd: String,
        topListener: TSdataFetched,
    ) {
        var totalRez: TSmainClass = TSmainClass()
        val listener: HTTPCallFinished =
            object : HTTPCallFinished {
                override fun callFinished(value: String?) {
                    //dfinirat jedan TSdatafetched interface, u zadnjoj liniji se taj poziva
                    //ako je rezultat manji od 8000 onda se poziva glavni top listener

                    var data: TSmainClass? = null
                    if(value.isNullOrEmpty()){
                        topListener.onDataRecived(null)
                    }
                    else {
                        data = gson.fromJson(value, TSmainClass::class.java)
                        if (data!!.feeds.size == 0) {
                            totalRez.channel = data!!.channel
                            topListener.onDataRecived(totalRez)
                        }
                        //if(data.feeds[0].entry_id == 1){
                        if (data.feeds.size < 8000) {
                            totalRez.feeds = data.feeds + totalRez.feeds
                            totalRez.channel = data.channel
                            topListener.onDataRecived(totalRez)

                        } else {
                            totalRez.feeds = data.feeds + totalRez.feeds

                            val localListener: TSdataFetched =
                                object : TSdataFetched {
                                    override fun onDataRecived(data: TSmainClass?) {
                                        if(data != null){
                                            val totalData: TSmainClass = TSmainClass()
                                            totalData.channel = data.channel;
                                            totalData.feeds = data.feeds + totalRez.feeds

                                            topListener.onDataRecived(totalData)
                                        }
                                        else{
                                            topListener.onDataRecived(null)
                                        }
                                    }
                                }
                            fetchInterval(chId,
                                APIkey,
                                timeStampStart,
                                totalRez.feeds[0].created_at,
                                localListener)
                        }

                    }

                }
            }
        val fullLink = constructBasicUrl(chId,
            APIkey) + "&start=" + ISOtoTSinputFormat(timeStampStart) + "&end=" + ISOtoTSinputFormat(
            timeStampEnd)
        HTTPcalls.requestCall(fullLink, listener)
    }

    fun fetchRecentAndSave(
        chId: String,
        APIkey: String,
        timeStampStart: String,
        listener: DBinserted,
    ) {
        var unix30minsLess: Long = 9
        if (timeStampStart.length > 5) {
            unix30minsLess = CalendarToUnix(ISOtoCalendar(timeStampStart)) - (60 * 30)
        }
        val startTimeStampMOdified = CalendarToIso(UnixToCalendar(unix30minsLess)!!)

        val randGenerator = Random(Calendar.getInstance().timeInMillis)

        val randSeconds = randGenerator.nextInt() % 60
        val randMinutes = randGenerator.nextInt() % 60
        val randHours = randGenerator.nextInt() % 24
        val randDay = randGenerator.nextInt() % 28 + 1
        val randMonth = randGenerator.nextInt() % 12 + 1

        var secondsString = randSeconds.absoluteValue.toString()
        while (secondsString.length < 2) {

            secondsString = "0$secondsString"
        }

        var minutesString = randMinutes.absoluteValue.toString()
        while (minutesString.length < 2) {
            minutesString = "0$minutesString"
        }
        var hourString = randHours.absoluteValue.toString()
        while (hourString.length < 2) {
            hourString = "0$hourString"
        }

        var dayString = randDay.absoluteValue.toString()
        while (dayString.length < 2) {
            dayString = "0$dayString"
        }
        var monthString = randMonth.absoluteValue.toString()
        while (monthString.length < 2) {
            monthString = "0$monthString"
        }

        val endTime = "2100-$monthString-${dayString}T$hourString:$minutesString:${secondsString}Z"

        val listener2: TSdataFetched = object : TSdataFetched {
            override fun onDataRecived(data: TSmainClass?) {
                if(data != null){
                    val LSlist: MutableList<LocationSample> =
                        locDataConverter().TSobjectToLocationSamples_s(data, APIkey)
                    deviceRepository.insertMultipleinsertsLocationSamples(LSlist)
                    deviceRepository.updatedLastUpdated(LSlist[LSlist.size - 1].created_at,
                        Device(0, "", chId, APIkey, "", ""))
                    listener.onDBinsertFinished()
                }
                else{
                    listener.onDBinsertFinished()
                }
            }
        }
        DeviceDataFetcher().fetchInterval(chId,
            APIkey,
            startTimeStampMOdified,
            endTime,
            listener2)
    }


    fun fetchAllandSave(chId: String, APIkey: String, ended: genericListener?) {
        val listener: TSdataFetched = object : TSdataFetched {
            override fun onDataRecived(data: TSmainClass?) {
                if(data != null){
                    val LSlist: MutableList<LocationSample> =
                        locDataConverter().TSobjectToLocationSamples_s(data, APIkey)

                    deviceRepository.insertMultipleinsertsLocationSamples(LSlist)
                    deviceRepository.updatedLastUpdated(LSlist[LSlist.size - 1].created_at,
                        Device(0, "", chId, APIkey, "", ""))
                }
                ended?.callEnded()
            }
        }
        DeviceDataFetcher().fetchInterval(chId,
            APIkey,
            "2000-01-01T00:00:00Z",
            "2100-01-01T00:00:00Z",
            listener)
    }


    fun constructBasicUrl(chId: String, APIkey: String): String {
        return "https://api.thingspeak.com/channels/${chId}/feeds.json?api_key=${APIkey}"
    }
}