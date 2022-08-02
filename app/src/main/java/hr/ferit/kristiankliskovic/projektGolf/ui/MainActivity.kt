package hr.ferit.kristiankliskovic.projektGolf.ui

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import hr.ferit.kristiankliskovic.projektGolf.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("ima li te", "eo me1")
        binding = ActivityMainBinding.inflate(layoutInflater)
        Log.i("ima li te", "eo me2")
        setContentView(binding.root)
        Log.i("ima li te", "eo m3")

    }

    companion object {
        lateinit var application: Application
    }
}