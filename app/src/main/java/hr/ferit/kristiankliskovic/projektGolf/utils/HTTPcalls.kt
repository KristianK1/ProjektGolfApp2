package hr.ferit.kristiankliskovic.projektGolf.utils

import android.net.MacAddress
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import hr.ferit.kristiankliskovic.projektGolf.mainSomething

object HTTPcalls {
    // Instantiate the RequestQueue.
    val queue = Volley.newRequestQueue(mainSomething.application)
    val url = "https://api.thingspeak.com/channels/1120413/feeds.json?api_key=SL9M2RUMWFGH5DIS&results=9000000000"

    // Request a string response from the provided URL.

    fun requestCall(url: String, randValue: Int, listener: ){
        Log.i("httpVV", "STARTED")
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->

                Log.i("httpVV", response);
            },
            { error ->
                Log.i("httpVV", error.toString())
            }
        )

        queue.add(stringRequest)

    }

}