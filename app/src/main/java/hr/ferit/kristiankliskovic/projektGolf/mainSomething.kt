package hr.ferit.kristiankliskovic.projektGolf

import android.app.Application
import android.util.Log
import android.view.KeyEvent
import com.google.gson.Gson
import hr.ferit.kristiankliskovic.projektGolf.utils.firebaseComm

class mainSomething: Application() {

    override fun onCreate() {
        super.onCreate()
        application = this
        firebaseComm.hello()
    }

    companion object{
        lateinit var application: Application
    }
}