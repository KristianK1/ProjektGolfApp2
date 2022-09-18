package hr.ferit.kristiankliskovic.projektGolf.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import hr.ferit.kristiankliskovic.projektGolf.model.user


object firebaseComm {
    private var database: DatabaseReference
    var users: ArrayList<user> = arrayListOf()
    val firebaseReady: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }
    var firstRun = true;

    init {
        Log.i("firebaseEE", "prije inita")

        database = FirebaseDatabase.getInstance().reference

        database.child("users").addValueEventListener(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val value = snapshot.value
                    val jsonValue = Gson().toJson(value)
                    Log.i("firebaseJson", jsonValue)
                    val builder = GsonBuilder()
                    builder.setPrettyPrinting()
                    val res = builder.create().fromJson(jsonValue, Array<user>::class.java)
                    users.clear()
                    users.addAll(res)
                    Log.i("firebaseJson", Gson().toJson(res))

                } catch (e: Throwable) {
                    users = arrayListOf()
                }

                for (it in users) {
                    Log.i("usersList", it.username + " " + it.password)
                }
                if(firstRun){
                    firstRun = false
                    firebaseReady.value = true
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("firebaseIspis", "cenceled...")
            }
        })

    }

    fun hello() {

    }

    fun checkCreds(user: user): user? {
        Log.i("firebaseIspis", Gson().toJson(users));
        for (it in users) {
            if (it.username == user.username && it.password == user.password) {
                return it
            }
        }
        return null
    }

    fun addUser(newUser: user) {
        database.child("users/${users.size}").setValue(newUser)
    }

    fun changeUser(user: user) {
        for(devs in user.devices){
            devs.lastRefreshed = ""
        }
        Log.i("changeUser", Gson().toJson(user))
        for ((index, value) in users.withIndex()) {
            if (value.username == user.username) {
                Log.i("changeUser", value.username)
                database.child("users/$index").setValue(user)
            }
        }
    }

    fun deleteUser(user: user){
        for ((index, value) in users.withIndex()) {
            if (value.username == user.username) {
                users.remove(value)
                database.child("users").setValue(users)
            }
        }
    }
}
