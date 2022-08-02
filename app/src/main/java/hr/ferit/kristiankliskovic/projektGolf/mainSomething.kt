package hr.ferit.kristiankliskovic.projektGolf

import android.app.Application

class mainSomething: Application() {

    override fun onCreate() {
        super.onCreate()
        application = this
    }

    companion object{
        lateinit var application: Application
    }
}