package hr.ferit.kristiankliskovic.projektGolf.ui.mainFragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import hr.ferit.kristiankliskovic.projektGolf.databinding.MainScreenBinding
import hr.ferit.kristiankliskovic.projektGolf.model.TSmainClass
import hr.ferit.kristiankliskovic.projektGolf.utils.DeviceDataFetcher
import hr.ferit.kristiankliskovic.projektGolf.utils.TSdataFetched
import hr.ferit.kristiankliskovic.projektGolf.utils.locDataConverter


class MainFragment: Fragment() {

    private lateinit var binding: MainScreenBinding
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainScreenBinding.inflate(layoutInflater)
        binding.btSett.setOnClickListener{
            val action = MainFragmentDirections.actionMainFragmentToSettingsFragment()
            findNavController().navigate(action)
        }
        val supportMapFragment =
            childFragmentManager.findFragmentById(hr.ferit.kristiankliskovic.projektGolf.R.id.google_map) as SupportMapFragment?

        supportMapFragment!!.getMapAsync( OnMapReadyCallback(){
            onMapReady(it)
        })
        test()

        return binding.root
    }

    fun onMapReady(p0: GoogleMap) {
        Log.i("mapTTR", "ready")
        map = p0

        val osijek = LatLng(45.55111, 18.69389)
        map.addMarker(MarkerOptions().position(osijek).title("Marker in Osijek"))
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        map.uiSettings.isZoomControlsEnabled = true
        map.moveCamera(CameraUpdateFactory.newLatLng(osijek))
    }


    fun JSONtest(){
        val test: String = "{\"channel\":{\"id\":1120413,\"name\":\"Golf\",\"description\":\"personal project\",\"latitude\":\"45.0\",\"longitude\":\"18.0\",\"field1\":\"data\",\"field2\":\"flags\",\"created_at\":\"2020-08-20T21:07:12Z\",\"updated_at\":\"2021-05-27T17:14:20Z\",\"elevation\":\"123\",\"last_entry_id\":16393},\"feeds\":[{\"created_at\":\"2022-08-03T20:36:50Z\",\"entry_id\":16392,\"field1\":\",\",\"field2\":null},{\"created_at\":\"2022-08-03T20:37:20Z\",\"entry_id\":16393,\"field1\":\"2LTTmMn1M0\",\"field2\":null}]}"
        locDataConverter().convertJSONtoTSObject(test)
    }

    fun test(){
        val listener: TSdataFetched = object : TSdataFetched{
            override fun onDataRecived(data: TSmainClass) {
                Log.i("dataaa", Gson().toJson(data))
                Log.i("dataaaa", "" +data.feeds?.size)
            }
        }
        DeviceDataFetcher().fetchInterval("1120413", "SL9M2RUMWFGH5DIS", "2018-07-20T01:58:07Z", "2022-08-03T01:58:00Z", listener)
    }
}