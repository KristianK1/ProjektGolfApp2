package hr.ferit.kristiankliskovic.projektGolf.utils

import android.location.GnssAntennaInfo
import android.util.Log
import com.android.volley.Response
import com.google.gson.Gson
import hr.ferit.kristiankliskovic.projektGolf.data.DeviceDao
import hr.ferit.kristiankliskovic.projektGolf.data.DeviceRepository
import hr.ferit.kristiankliskovic.projektGolf.data.DeviceRepositoryImpl
import hr.ferit.kristiankliskovic.projektGolf.di.DeviceRepositoryFactory
import hr.ferit.kristiankliskovic.projektGolf.model.Device
import hr.ferit.kristiankliskovic.projektGolf.model.LocationSample
import hr.ferit.kristiankliskovic.projektGolf.model.TSmainClass
import org.xml.sax.Locator

class DeviceDataFetcher {
    val gson = Gson()
    val deviceRepository = DeviceRepositoryFactory.deviceRepository


    fun fetchInterval(chId: String, APIkey: String, timeStampStart: String, timeStampEnd: String, topListener: TSdataFetched){
        var totalRez: TSmainClass = TSmainClass()
        Log.i("dataaa", "heyyydd")
        val listener: HTTPCallFinished =
            object : HTTPCallFinished{
                override fun callFinished(value: String?) {
                    //dfinirat jedan TSdatafetched interface, u zadnjoj liniji se taj poziva
                    //ako je rezultat manji od 8000 onda se poziva glavni top listener

                    Log.i("dataaa", "nakon")
                    if(value.isNullOrEmpty() == false)
                        Log.i("dataaa", value!!)
                    else{
                        Log.i("dataaa", "bogiranje")
                    }
                    val data: TSmainClass = gson.fromJson(value, TSmainClass::class.java)

                    try{
                        //totalRez.channel = data.channel
                    }catch(e: Throwable){
                        Log.i("dataaa error", "err")
                    }
                    if(data.feeds.size == 0){
                        totalRez.channel = data.channel
                        topListener.onDataRecived(totalRez)
                    }
                    if(data.feeds[0].entry_id == 1){
                        Log.i("dataaa", "prije = " + data.feeds.size + " (novi)+(stari) " + totalRez.feeds.size)
                        totalRez.feeds = data.feeds + totalRez.feeds

                        Log.i("dataaa", "poslije + = " + totalRez.feeds.size)
                        Log.i("dataaa", "gotovo")
                        topListener.onDataRecived(totalRez)

                    }
                    else{
                        Log.i("dataaa", "rekurzija")
                        Log.i("dataaa", "prije = " + data.feeds.size + " (novi)+(stari) " + totalRez.feeds.size)
                        totalRez.feeds = data.feeds + totalRez.feeds

                        Log.i("dataaa", "poslije + = " + totalRez.feeds.size)
                        val localListener: TSdataFetched =
                            object : TSdataFetched{
                                override fun onDataRecived(data: TSmainClass) {
                                    val totalData:TSmainClass = TSmainClass()
                                    totalData.channel = data.channel;
                                    Log.i("dataaa LAST list", "prije = " + data.feeds.size + " (novi)+(stari) " + totalRez.feeds.size)
                                    totalData.feeds = data.feeds + totalRez.feeds

                                    Log.i("dataaa", "poslije + = " + totalData.feeds.size)
                                    topListener.onDataRecived(totalData)
                                }
                            }
                        fetchInterval(chId, APIkey,timeStampStart, totalRez.feeds[0].created_at, localListener)
                    }



                }
            }
        Log.i("dataaa", "prije linka")

        val fullLink = constructBasicUrl(chId, APIkey) + "&start=" + ISOtoTSinputFormat(timeStampStart) + "&end=" + ISOtoTSinputFormat(timeStampEnd)
        Log.i("dataaa link", fullLink)
        HTTPcalls.requestCall(fullLink, listener)
    }

    fun fetchAllandSave(chId: String, APIkey: String){

        val listener: TSdataFetched = object : TSdataFetched{
            override fun onDataRecived(data: TSmainClass) {
                Log.i("dataaa", Gson().toJson(data))
                Log.i("dataaaa", "" +data.feeds?.size)
                val LSlist: MutableList<LocationSample> = locDataConverter().TSobjectToLocationSamples_s(data, APIkey)
                for(LS in LSlist){
                    Log.i("saveData", "one")
                    deviceRepository.insertLocationSamples(LS)
                }
            }
        }
        DeviceDataFetcher().fetchInterval("1120413", "SL9M2RUMWFGH5DIS", "2000-01-01T00:00:00Z", "2100-01-01T00:00:00Z", listener)
    }


    fun constructBasicUrl(chId: String, APIkey: String): String{
        return "https://api.thingspeak.com/channels/${chId}/feeds.json?api_key=${APIkey}"
    }
}