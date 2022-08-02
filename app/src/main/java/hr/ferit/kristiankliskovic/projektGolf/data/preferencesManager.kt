package hr.ferit.kristiankliskovic.projektGolf.data

import android.content.Context
import hr.ferit.kristiankliskovic.projektGolf.mainSomething

class preferencesManager {

    companion object {
        const val PREFS_FILE = "MyPreferences"
        const val PREFS_KEY_CURR_DEVICE = "currDeviceName"
    }

    fun saveCurrDeviceName(username: String) {
        val sharedPreferences = mainSomething.application.getSharedPreferences(
            PREFS_FILE, Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putString(PREFS_KEY_CURR_DEVICE, username)
        editor.apply()
    }
    fun getCurrDeviceName(): String? {
        val sharedPreferences = mainSomething.application.getSharedPreferences(
            PREFS_FILE, Context.MODE_PRIVATE
        )
        return sharedPreferences.getString(PREFS_KEY_CURR_DEVICE, "")
    }

}