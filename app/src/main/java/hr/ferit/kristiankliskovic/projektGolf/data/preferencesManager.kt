package hr.ferit.kristiankliskovic.projektGolf.data

import android.content.Context
import com.google.gson.Gson
import hr.ferit.kristiankliskovic.projektGolf.mainSomething
import hr.ferit.kristiankliskovic.projektGolf.model.Device
import hr.ferit.kristiankliskovic.projektGolf.model.user

object preferencesManager {

    const val PREFS_FILE = "MyPreferences"
    const val PREFS_KEY_CURR_DEVICE = "currDeviceName"
    const val BASIC_ts_key = "timeStamp_"
    const val HISTORY_key = "historyValue"
    const val CHOICE_KEY = "choiceKey"
    const val USER_KEY = "userKey"

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
            sharedPreferences.getString(PREFS_KEY_CURR_DEVICE, "")!!
        } catch (e: Throwable) {
            "";
        }
    }

    fun saveTimestamp(which: Int, ts: String) {
        val sharedPreferences = mainSomething.application.getSharedPreferences(
            PREFS_FILE, Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putString(BASIC_ts_key + which, ts)
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

    fun setUser(user: user) {
        val sharedPreferences = mainSomething.application.getSharedPreferences(
            PREFS_FILE, Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putString(USER_KEY, Gson().toJson(user))
        editor.apply()
    }

    fun removeUser(){
        val sharedPreferences = mainSomething.application.getSharedPreferences(
            PREFS_FILE, Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putString(USER_KEY, "")
        editor.apply()
    }

    fun getUser(): user? {
        var myUser: String? = null
        try {
            val sharedPreferences = mainSomething.application.getSharedPreferences(
                PREFS_FILE, Context.MODE_PRIVATE
            )
            myUser = sharedPreferences.getString(USER_KEY, "")!!
        } catch (e: Throwable) {
        }
        return Gson().fromJson(myUser, user::class.java)
    }
}