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
        val randSeconds = randGenerator.nextInt()%60;
        val randMinutes = randGenerator.nextInt()%60 + 10;
        val randHours = randGenerator.nextInt()%24 + 10;
        val randDay =  randGenerator.nextInt()%28 + 10;
        val randMonth =  randGenerator.nextInt()%12 + 10;

        var secondsString = randSeconds.toString()
        if(secondsString.length == 1) secondsString = "0$secondsString"

        var minutesString = randMinutes.toString()
        if(minutesString.length == 1) minutesString = "0$minutesString"

        var hourString = randHours.toString()
        if(hourString.length == 1) hourString = "0$hourString"

        var dayString = randDay.toString()
        if(dayString.length == 1) dayString = "0$dayString"

        var monthString = randMonth.toString()
        if(monthString.length == 1) monthString = "0$monthString"

        val endTime = "2100-$monthString-${dayString}T$hourString:$randMinutes:${randSeconds}Z"
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