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
        Log.i("dataaa", "heyyydd")
        val listener: HTTPCallFinished =
            object : HTTPCallFinished {
                override fun callFinished(value: String?) {
                    //dfinirat jedan TSdatafetched interface, u zadnjoj liniji se taj poziva
                    //ako je rezultat manji od 8000 onda se poziva glavni top listener

                    Log.i("dataaa", "nakon")
                    if (value.isNullOrEmpty() == false)
                        Log.i("dataaa", value!!)
                    else {
                        Log.i("dataaa", "bogiranje")
                    }
                    val data: TSmainClass = gson.fromJson(value, TSmainClass::class.java)

                    try {
                        //totalRez.channel = data.channel
                    } catch (e: Throwable) {
                        Log.i("dataaa error", "err")
                    }
                    if (data.feeds.size == 0) {
                        totalRez.channel = data.channel
                        topListener.onDataRecived(totalRez)
                    }
                    //if(data.feeds[0].entry_id == 1){
                    if (data.feeds.size < 8000) {
                        Log.i("dataaa",
                            "prije = " + data.feeds.size + " (novi)+(stari) " + totalRez.feeds.size)
                        totalRez.feeds = data.feeds + totalRez.feeds
                        totalRez.channel = data.channel
                        Log.i("dataaa", "poslije + = " + totalRez.feeds.size)
                        Log.i("dataaa", "gotovo")
                        topListener.onDataRecived(totalRez)

                    } else {
                        Log.i("dataaa", "rekurzija")
                        Log.i("dataaa",
                            "prije = " + data.feeds.size + " (novi)+(stari) " + totalRez.feeds.size)
                        totalRez.feeds = data.feeds + totalRez.feeds

                        Log.i("dataaa", "poslije + = " + totalRez.feeds.size)
                        val localListener: TSdataFetched =
                            object : TSdataFetched {
                                override fun onDataRecived(data: TSmainClass) {
                                    val totalData: TSmainClass = TSmainClass()
                                    totalData.channel = data.channel;
                                    Log.i("dataaa LAST list",
                                        "prije = " + data.feeds.size + " (novi)+(stari) " + totalRez.feeds.size)
                                    totalData.feeds = data.feeds + totalRez.feeds

                                    Log.i("dataaa", "poslije + = " + totalData.feeds.size)
                                    topListener.onDataRecived(totalData)
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
        Log.i("dataaa", "prije linka")

        val fullLink = constructBasicUrl(chId,
            APIkey) + "&start=" + ISOtoTSinputFormat(timeStampStart) + "&end=" + ISOtoTSinputFormat(
            timeStampEnd)
        Log.i("dataaa link", fullLink)
        HTTPcalls.requestCall(fullLink, listener)
    }

    fun fetchRecentAndSave(
        chId: String,
        APIkey: String,
        timeStampStart: String,
        listener: DBinserted,
    ) {
        var unix30minsLess: Long = 9
        if(timeStampStart.length > 5) {
            unix30minsLess = CalendarToUnix(ISOtoCalendar(timeStampStart)) - (60 * 30)
        }
        Log.i("dataaa", "unix mid value " + unix30minsLess)
        val startTimeStampMOdified = CalendarToIso(UnixToCalendar(unix30minsLess)!!)
        Log.i("dataaa", "modifiredTime" + startTimeStampMOdified)

        val randGenerator = Random(Calendar.getInstance().timeInMillis)
        Log.i("dataaa", "hello1")

        val randSeconds = randGenerator.nextInt()%60
        Log.i("dataaa", "hello2")
        val randMinutes = randGenerator.nextInt()%60
        val randHours = randGenerator.nextInt()%24
        val randDay =  randGenerator.nextInt()%28 + 1
        val randMonth =  randGenerator.nextInt()%12 + 1
        Log.i("dataaa", "hello3")

        var secondsString = randSeconds.absoluteValue.toString()
        while(secondsString.length < 2) {
            Log.i("dataaa", "hello33")
            Log.i("dataaa", "sekunde" + secondsString)

            secondsString = "0$secondsString"
        }
        Log.i("dataaa", "sekunde" + secondsString)

        Log.i("dataaa", "hello4")

        var minutesString = randMinutes.absoluteValue.toString()
        while(minutesString.length < 2) {
            Log.i("dataaa", "hello44")
            Log.i("dataaa", "minute" + minutesString)
            minutesString = "0$minutesString"
        }
        Log.i("dataaa", "minute" + minutesString)

        Log.i("dataaa", "hello5")

        var hourString = randHours.absoluteValue.toString()
        while(hourString.length < 2){
            Log.i("dataaa", "hello55")
            Log.i("dataaa", "sati" + hourString)
            hourString = "0$hourString"
        }
        Log.i("dataaa", "sati" + hourString)

        Log.i("dataaa", "hello6")

        var dayString = randDay.absoluteValue.toString()
        while(dayString.length < 2) {
            Log.i("dataaa", "hello66")
            Log.i("dataaa", "dan" + dayString)
            dayString = "0$dayString"
        }
        Log.i("dataaa", "dan" + dayString)

        Log.i("dataaa", "hello7")

        var monthString = randMonth.absoluteValue.toString()
        while(monthString.length < 2){
            Log.i("dataaa", "hello77")
            Log.i("dataaa", "mjesec" + monthString)
            monthString = "0$monthString"
        }
        Log.i("dataaa", "mjesec" + monthString)

        val endTime = "2100-$monthString-${dayString}T$hourString:$minutesString:${secondsString}Z"

        Log.i("dataaa", endTime)
        Log.i("dataaa update", "started Save")
        val listener2: TSdataFetched = object : TSdataFetched {
            override fun onDataRecived(data: TSmainClass) {
                val LSlist: MutableList<LocationSample> =
                    locDataConverter().TSobjectToLocationSamples_s(data, APIkey)
                deviceRepository.insertMultipleinsertsLocationSamples(LSlist)
                deviceRepository.updatedLastUpdated(LSlist[LSlist.size - 1].created_at,
                    Device(0, "", chId, APIkey, "", ""))
                Log.i("dataaa update", "finished Insert")
                listener.onDBinsertFinished()
            }
        }
        DeviceDataFetcher().fetchInterval(chId,
            APIkey,
            startTimeStampMOdified,
            endTime,
            listener2)
    }


    fun  fetchAllandSave(chId: String, APIkey: String, ended: genericListener?) {
        Log.i("dataaa", "started Save")
        val listener: TSdataFetched = object : TSdataFetched {
            override fun onDataRecived(data: TSmainClass) {
                Log.i("bitnoTT", Gson().toJson(data))

                Log.i("bitnoTT", "" + data.feeds?.size)
                val LSlist: MutableList<LocationSample> =
                    locDataConverter().TSobjectToLocationSamples_s(data, APIkey)

                deviceRepository.insertMultipleinsertsLocationSamples(LSlist)
                deviceRepository.updatedLastUpdated(LSlist[LSlist.size - 1].created_at,
                    Device(0, "", chId, APIkey, "", ""))
                Log.i("dataaa", "finished Insert")
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