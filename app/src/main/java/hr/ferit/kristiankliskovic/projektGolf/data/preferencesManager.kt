package hr.ferit.kristiankliskovic.projektGolf.data

import android.content.Context
import hr.ferit.kristiankliskovic.projektGolf.mainSomething

class preferencesManager {

    companion object {
        const val PREFS_FILE = "MyPreferences"
        const val PREFS_KEY_CURR_DEVICE = "currDeviceName"
        const val BASIC_ts_key = "timeStamp_"
        const val HISTORY_key = "historyValue"
        const val CHOICE_KEY = "choiceKey"
    }

    fun saveCurrDeviceName(username: String) {
        val sharedPreferences = mainSomething.application.getSharedPreferences(
            PREFS_FILE, Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putString(PREFS_KEY_CURR_DEVICE, username)
        editor.apply()
    }

    fun getCurrDeviceName(): String {
        return try {
            val sharedPreferences = mainSomething.application.getSharedPreferences(
                PREFS_FILE, Context.MODE_PRIVATE
            )
            sharedPreferences.getString(
                hr.ferit.kristiankliskovic.projektGolf.data.preferencesManager.Companion.PREFS_KEY_CURR_DEVICE,
                ""
            )!!
        } catch (e: Throwable) {
            "";
        }

    }

    fun saveTimestamp(which: Int, ts1: String) {
        val sharedPreferences = mainSomething.application.getSharedPreferences(
            PREFS_FILE, Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putString(BASIC_ts_key + which, ts1)
        editor.apply()
    }


    fun getTimestamp(which: Int): String {
        return try {
            val sharedPreferences = mainSomething.application.getSharedPreferences(
                PREFS_FILE, Context.MODE_PRIVATE
            )
            return sharedPreferences.getString(BASIC_ts_key + which, "")!!
        } catch (e: Throwable) {
            "";
        }
    }

    fun setHistory(his: String) {
        val sharedPreferences = mainSomething.application.getSharedPreferences(
            PREFS_FILE, Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putString(HISTORY_key, his)
        editor.apply()
    }

    fun getHistory(): String {
        return try {
            val sharedPreferences = mainSomething.application.getSharedPreferences(
                PREFS_FILE, Context.MODE_PRIVATE
            )
            return sharedPreferences.getString(HISTORY_key, "")!!

        } catch (e: Throwable) {
            "";
        }
    }

    fun setChoice(c: Int) {
        val sharedPreferences = mainSomething.application.getSharedPreferences(
            PREFS_FILE, Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putInt(CHOICE_KEY, c)
        editor.apply()
    }

    fun getChoice(): Int {
        return try {
            val sharedPreferences = mainSomething.application.getSharedPreferences(
                PREFS_FILE, Context.MODE_PRIVATE
            )
            return sharedPreferences.getInt(CHOICE_KEY, 0)!!
        } catch (e: Throwable) {
            -1;
        }
    }
}