package hr.ferit.kristiankliskovic.projektGolf.utils

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import hr.ferit.kristiankliskovic.projektGolf.model.LocationSample
import hr.ferit.kristiankliskovic.projektGolf.model.TSmainClass


class locDataConverter {
    val gson = Gson()

    fun convertJSONtoTSObject(rawData: String): TSmainClass {
        val parsed = gson.fromJson(rawData, TSmainClass::class.java)
        Log.i("gsonTry", parsed.channel.created_at)
        return parsed
    }

    fun TSobjectToLocationSamples_s(TSobj: TSmainClass, APIkey: String): MutableList<LocationSample>{
        val LSlist: MutableList<LocationSample> = arrayListOf()
        for(TSentry in TSobj.feeds){
            if(TSentry.field1.isNullOrEmpty()) continue
            val locsStrings = TSentry.field1!!.split("*")
            for ((index, codedLoc) in locsStrings.withIndex()){
                val decodedLocation = shortLocationStringToCoors(codedLoc)
                val LS = LocationSample(0, TSobj.channel.id.toString(), APIkey, TSentry.entry_id, TSentry.created_at, decodedLocation.longitude, decodedLocation.latitude, -1.0, index == locsStrings.size - 1)
                LSlist.add(LS)
            }
        }
        return LSlist
    }

    fun shortLocationStringToCoors(input: String): LatLng{

        val base = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.:"
        //velicina paketa 11 charova, ponekad 10
        //zadnji char je onaj dodatni character

        //velicina paketa 11 charova, ponekad 10
        //zadnji char je onaj dodatni character
        if ((input.length == 11 || input.length == 10 || input.length == 1) == false) {
            return LatLng(93.0,183.0)
        }
        if (input.length == 1) {
            return LatLng(92.0,182.0)
        }

        var binary = ""
        for (i in 0..9) {
            val value = base.indexOf("" + input.get(i))
            var value_bin = Integer.toBinaryString(value)
            while (value_bin.length < 6) {
                value_bin = "0$value_bin"
            }
            binary += value_bin
        }

        //60 znakova

        //60 znakova
        var sum = 0
        for (i in 0..56) {
            if (binary[i] == '1') sum++
        }
        sum = sum % 8
        var Checksum = ""
        for (i in 57..59) Checksum += binary[i]
        if (Checksum.toInt(2) != sum) {
            Log.i("tag_binary", "neispravno dekodiranje")
        }

        //X

        //X
        val X_sign = if (binary[0] == '0') 1 else -1

        var X_int_str = ""
        for (i in 1..8) X_int_str += binary[i]
        val X_int_int = X_int_str.toInt(2)


        var X_dec_str = ""
        for (i in 9..28) X_dec_str += binary[i]
        var X_dec_dec = X_dec_str.toInt(2).toDouble()
        X_dec_dec /= 1000000.0


        val xx = (X_sign * (X_int_int + X_dec_dec))

        //Y

        //Y
        val Y_sign = if (binary[29] == '0') 1 else -1

        var Y_int_str = ""
        for (i in 30..36) Y_int_str += binary[i]
        val Y_int_int = Y_int_str.toInt(2)

        Log.i("tag_binary", "" + Y_int_str)

        var Y_dec_str = ""
        for (i in 37..56) Y_dec_str += binary[i]
        var Y_dec_dec = Y_dec_str.toInt(2).toDouble()
        Y_dec_dec /= 1000000.0


        val yy = (Y_sign * (Y_int_int + Y_dec_dec))
        return LatLng(yy,xx)
    }
}
