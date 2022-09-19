package hr.ferit.kristiankliskovic.projektGolf.ui

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import hr.ferit.kristiankliskovic.projektGolf.databinding.ActivityMainBinding
import java.nio.file.attribute.AclEntryPermission

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        activity = this@MainActivity
        setContentView(binding.root)
//        requestPermission(arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
//            BLUETOOTH_PERMISSION_CODE)
//
//        requestPermission(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
//            LOCATION_PERMISSION_CODE)

    }

    companion object {
        lateinit var application: Application
        lateinit var activity: Activity
        const val LOCATION_PERMISSION_CODE = 100
        const val BLUETOOTH_PERMISSION_CODE = 101
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        Log.i("perms", "onRequetResult")
        Log.i("perms", Gson().toJson(requestCode))
        Log.i("perms", Gson().toJson(permissions))
        Log.i("perms", Gson().toJson(grantResults))

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i("perms", "after super")
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity,
                    "Location Permission Granted",
                    Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity,
                    "Location Permission Denied",
                    Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == BLUETOOTH_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity,
                    "Bluetooth Permission Granted",
                    Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity,
                    "Bluetooth Permission Denied",
                    Toast.LENGTH_SHORT).show()
            }
        }

    }
}