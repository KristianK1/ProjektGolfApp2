package hr.ferit.kristiankliskovic.projektGolf.ui.mainFragment
import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import hr.ferit.kristiankliskovic.projektGolf.data.preferencesManager
import hr.ferit.kristiankliskovic.projektGolf.databinding.MainScreenBinding
import hr.ferit.kristiankliskovic.projektGolf.di.DeviceRepositoryFactory
import hr.ferit.kristiankliskovic.projektGolf.model.Device
import hr.ferit.kristiankliskovic.projektGolf.model.LocationSample
import hr.ferit.kristiankliskovic.projektGolf.utils.*
import java.util.*


class MainFragment: Fragment() {

    private var appIsBusy: Boolean = false
    private lateinit var binding: MainScreenBinding
    private var timer: Timer = Timer()
    private lateinit var map: GoogleMap
    private val deviceRepository = DeviceRepositoryFactory.deviceRepository
    private var currentDevice: Device? = updateCurrentDevice()
    private var LocationDisplayList: MutableList<LocationSample> = mutableListOf()

    private var startInterval: Long? = null
    private var endInterval: Long? = null
    private var centerMap: Boolean? = true
    private var displayMarkers: Boolean? = false
    private var liveTracking: Boolean? = true
    private var currCenterLocation: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = MainScreenBinding.inflate(layoutInflater)
        getSettings()
        binding.btSett.setOnClickListener {
            if(appIsBusy == false){
                val action = MainFragmentDirections.actionMainFragmentToSettingsFragment()
                findNavController().navigate(action)
            }
        }

        if(centerMap == true)
            binding.centerView.setBackgroundColor(Color.GREEN)
        else
            binding.centerView.setBackgroundColor(Color.RED)
        binding.centerView.setOnClickListener {

            if(currCenterLocation != null){
                centerMap = true
                liveTracking = centerMap
                setUpLooper()
//                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currCenterLocation!!, 15F),400,null);
                binding.centerView.setBackgroundColor(Color.GREEN);
            }
        }

        val supportMapFragment =
            childFragmentManager.findFragmentById(hr.ferit.kristiankliskovic.projektGolf.R.id.google_map) as SupportMapFragment?


        supportMapFragment!!.getMapAsync( OnMapReadyCallback(){
            onMapReady(it)
        })

        currentDevice = updateCurrentDevice()
        return binding.root
    }

    fun setUpLooper(){
        if(timer != null){
            timer.cancel()
        }
        try{
            timer = Timer()
            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    Log.i("interval", "This function is called every 10 seconds.")
                    refresh()
                }
            }, 0, 10000)

//            timer.runCatching {
//                Log.i("interval", "catch" + Gson().toJson(this))
//                Toast.makeText(requireContext(), "CATCH", LENGTH_SHORT)
//            }
        } catch (e: Throwable){

        }
    }

    override fun onResume() {
        super.onResume()
        setUpLooper()

    }

    override fun onPause() {
        timer.cancel()
        timer = Timer()
        super.onPause()
    }


    fun onMapReady(p0: GoogleMap) {
        Log.i("mapTTR", "ready")
        map = p0
        if(currentDevice != null) {


            map.setOnCameraMoveStartedListener { reason ->
                if (reason == OnCameraMoveStartedListener.REASON_GESTURE) {
                    Log.i("cameraMap", "onCameraMoveStarted")
                    centerMap = false
                    liveTracking = centerMap
                    timer.cancel()
                    timer = Timer()
                    binding.centerView.setBackgroundColor(Color.RED)
                }
            }
//
//            binding.mapContainer.setOnTouchListener(object: View.OnTouchListener{
//                override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
//                    if(p1?.action == MotionEvent.ACTION_DOWN){
//                        Log.i("cameraMap", "touched")
//                    }
//                    return p0?.onTouchEvent(p1)?: true
//                }
//            })

//            refreshMap()
        } else
            try{
                map.clear()
            } catch( e: Throwable){}
    }

    fun getSettings(){

        var Name = "";
        try{
            Name = preferencesManager().getCurrDeviceName()
        } catch (e: Throwable) {
            Log.i("errrS", "FS");
        }
        if(Name == ""){
            Toast.makeText(context, "Nije postavljen uređaj", LENGTH_SHORT).show()
            startInterval = null
            endInterval = null
            displayMarkers = false
            currCenterLocation = null
            appIsBusy = false
            binding.centerView.setBackgroundColor(Color.RED);
            return
        }

        val startTime: Long
        val endTime: Long

        val displayChoice: Int = preferencesManager().getChoice()
        if(displayChoice == 0){
            Toast.makeText(context, "Nisu postavljeni parametri o dohvaćanju podataka", LENGTH_SHORT).show()
            startInterval = null
            endInterval = null
            displayMarkers = false
            currCenterLocation = null
            binding.centerView.setBackgroundColor(Color.RED);
            return
        }
        if(displayChoice == 1){
            val start_sp = preferencesManager().getTimestamp(1)
            val end_sp = preferencesManager().getTimestamp(2)
            if(!start_sp.isNullOrEmpty() && !end_sp.isNullOrEmpty()){
                Log.i("mapSettings_START", start_sp)
                Log.i("mapSettings_END", end_sp)
                startInterval = CalendarToUnix(ISOtoCalendar(start_sp))
                endInterval = CalendarToUnix(ISOtoCalendar(end_sp))
                Log.i("startIntervalw" , "" + startInterval)
                Log.i("startIntervalw", "" + endInterval)
                displayMarkers = false
            }
            else{
                Toast.makeText(context, "Nisu postavljeni parametri o dohvaćanju podataka", LENGTH_SHORT).show()
                startInterval = null
                endInterval = null
                displayMarkers = false
                return
            }

        }
        else{
            endInterval = Calendar.getInstance().timeInMillis / 1000
            startInterval = Calendar.getInstance().timeInMillis / 1000 - historyStringToSeconds(preferencesManager().getHistory())

            Log.i("startInterval", "" + startInterval)
            Log.i("startInterval", "" + endInterval)

            displayMarkers = false
            if(startInterval == endInterval){
                Toast.makeText(context, "Nisu postavljeni parametri o dohvaćanju podataka", LENGTH_SHORT).show()
                Log.i("startInterval", "err")
                startInterval = null
                endInterval = null
                displayMarkers = false
                return
            }
        }


    }

    fun updateCurrentDevice(): Device?{
        val Name = preferencesManager().getCurrDeviceName()

        Log.i("mainQ", "name " + Name)
        val devices = deviceRepository.deviceDao.getAllDevices()
        val curr = devices.find { o -> o.name == Name }
        Log.i("mainQ", "device " + Gson().toJson(currentDevice))
        return curr
    }

    private fun refresh() {

        Log.i("mainQ", "EO ME")
        if(currentDevice != null){
            appIsBusy = true;
            val tsStart = currentDevice!!.lastRefreshed
            Log.i("dataaa", "since When :" + tsStart)
            val listener: DBinserted =
                object : DBinserted {
                    override fun onDBinsertFinished() {
                        Log.i("mainQ", "prije selecta")
                        val Tstart = CalendarToIso(addTimeToCalendar(UnixToCalendar(startInterval!!)!!,0,0,-1,-1,0,0))
                        val Tend = CalendarToIso(addTimeToCalendar(UnixToCalendar(endInterval!!)!!,0,0,2,0,0,0))

                        val temp = deviceRepository.deviceDao.getLSfrom(currentDevice!!.channelId, currentDevice!!.ReadAPIkey, Tstart, Tend)
                        Log.i("mainQ", "temp " + temp.size)

                        LocationDisplayList = temp.toMutableList()
                        if(LocationDisplayList.size == 0){
                            LocationDisplayList = deviceRepository.deviceDao.getLastLSfrom(currentDevice!!.channelId, currentDevice!!.ReadAPIkey, Tend).toMutableList()
                        }
                        refreshMap()
                    }
                }
            DeviceDataFetcher().fetchRecentAndSave(currentDevice!!.channelId, currentDevice!!.ReadAPIkey, tsStart, listener)
        }
    }

    fun refreshMap(){
        updateCurrentDevice()
        getSettings()

        if(startInterval == null) {
            appIsBusy = false;
            return
        }

        val LL: ArrayList<LatLng> = ArrayList<LatLng>()

        var indexOfFirstLocation = -1
        var indexOfLastLocation = 0
        for((index,LS) in LocationDisplayList.withIndex()){
            val corrected_UNIX: Long = CalendarToUnix(isostring_toLocalCalendar(LS.created_at))
            if(corrected_UNIX > startInterval!!){
                if(corrected_UNIX < endInterval!!){
                    if(LS.latitude < 89.9 && LS.latitude > -89.9) {
                        if(indexOfFirstLocation == -1) indexOfFirstLocation = index
                        indexOfLastLocation = index
                        LL.add(LatLng(LS.latitude, LS.longitude))
                        Log.i("convertStuff_1", LS.created_at)

                    }
                }
                else{
                    break
                }
            }
        }

        Log.i("justOne", Gson().toJson(LocationDisplayList))

        val markers = mutableListOf<MarkerOptions>()
        if(indexOfFirstLocation == -1) {
            var marker: MarkerOptions = MarkerOptions()
            marker.position(LatLng(LocationDisplayList[LocationDisplayList.size - 1].latitude, LocationDisplayList[LocationDisplayList.size - 1].longitude))
            marker.title("Točka izvan zadanog intervala " + CalendartoMapMarkerString( isostring_toLocalCalendar(LocationDisplayList[LocationDisplayList.size - 1].created_at)))
            marker.zIndex(0F)
            marker.visible(true)
            markers.add(marker)
            currCenterLocation = LatLng(LocationDisplayList[LocationDisplayList.size - 1].latitude, LocationDisplayList[LocationDisplayList.size - 1].longitude)
            if(centerMap == true)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currCenterLocation!!,15F), 300,null)
        }
        else if(indexOfFirstLocation != indexOfLastLocation){// dvije razlicite lokacije za pocetak i kraj
            var marker: MarkerOptions = MarkerOptions()
            marker.position(LL.first())
            marker.title("Start point " + CalendartoMapMarkerString( isostring_toLocalCalendar(LocationDisplayList[indexOfFirstLocation].created_at)))
            marker.zIndex(0F)
            marker.visible(true)
            markers.add(marker)

            marker = MarkerOptions()
            marker.position(LL.last())
            marker.title("End point " + CalendartoMapMarkerString(isostring_toLocalCalendar(LocationDisplayList[indexOfLastLocation].created_at)))
            marker.zIndex(1F)
            marker.visible(true)
            markers.add(marker)

            currCenterLocation = LL.last()
            if(centerMap == true)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(LL.last(),15F), 300,null)
        }
        else if(indexOfFirstLocation == indexOfLastLocation){ //ista lokacija za pocetak i kraj
            var marker: MarkerOptions = MarkerOptions()
            marker.position(LL.first())
            marker.title(CalendartoMapMarkerString(isostring_toLocalCalendar(LocationDisplayList[indexOfLastLocation].created_at)))
            marker.zIndex(0F)
            marker.visible(true)
            markers.add(marker)
//            Toast.makeText(requireContext(), "Ne postoje zapisi o lokaciji u odabranom intervalu", LENGTH_SHORT).show()
            currCenterLocation = LL.last()
            if(centerMap == true)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(LL.last(),15F), 300,null)
        }


        var tankaCrnaLinija = PolylineOptions()
        tankaCrnaLinija.addAll(LL)


        map.clear()
        map.addPolyline(tankaCrnaLinija)
        for(marker in markers){
            Log.i("markers", Gson().toJson(marker))
            map.addMarker(marker)
        }

        appIsBusy = false;
    }

    fun test(){
        val str = "2022-08-06T20:00:01Z";
        val rez = CalendarToIso(isostring_toLocalCalendar(str))
        Log.i("testtt", rez)
    }

}
