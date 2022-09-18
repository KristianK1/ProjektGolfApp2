package hr.ferit.kristiankliskovic.projektGolf

import android.app.Application
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