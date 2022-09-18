package hr.ferit.kristiankliskovic.projektGolf.utils.httpAPI

import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import hr.ferit.kristiankliskovic.projektGolf.mainSomething

object HTTPcalls {
    // Instantiate the RequestQueue.
    private val queue = Volley.newRequestQueue(mainSomething.application)

    // Request a string response from the provided URL.

    fun requestCall(url: String,listener: HTTPCallFinished){

        Log.i("httpVV", "STARTED")
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                listener.callFinished(response);
                Log.i("httpVV", response);
            },
            { error ->
                listener.callFinished(null)
                Log.i("httpVV", error.toString())
            }
        )

        queue.add(stringRequest)

    }

}