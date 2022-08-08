package hr.ferit.kristiankliskovic.projektGolf.ui.addNewDeviceFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Response
import hr.ferit.kristiankliskovic.projektGolf.databinding.FragmentAddNewDeviceBinding
import hr.ferit.kristiankliskovic.projektGolf.di.DeviceRepositoryFactory
import hr.ferit.kristiankliskovic.projektGolf.model.Device
import hr.ferit.kristiankliskovic.projektGolf.utils.DeviceDataFetcher
import hr.ferit.kristiankliskovic.projektGolf.utils.HTTPCallFinished
import hr.ferit.kristiankliskovic.projektGolf.utils.HTTPcalls
import hr.ferit.kristiankliskovic.projektGolf.utils.TSdataFetched
import java.util.*

class AddNewDeviceFragment: Fragment() {

    private lateinit var device: Device
    private lateinit var binding: FragmentAddNewDeviceBinding
    private val deviceRepository = DeviceRepositoryFactory.deviceRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNewDeviceBinding.inflate(layoutInflater)
        binding.btAddDev.setOnClickListener{ addNewDevice() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    private fun addNewDevice(){
        Log.i("addNewDeviceBt", "ulazak u fun")
        var errors: String = "";
        if(binding.etName.text.toString().length < 4 || binding.etName.text.toString().length > 10){
            errors += "Ime mora sadržavati izemđu 4 i 10 znakova"
        }
        else if(binding.etChannelId.text.toString().length != 7){
            if(errors.isNotEmpty()) errors += "\r\n"
            errors += "Channel ID nema odgovarajući broj znakova"
        }
        else if(binding.etAPIkey.text.toString().length != 16){
            if(errors.isNotEmpty()) errors += "\r\n"
            errors += "API ključ nema odgovarajući broj znakova"
        }
        else if(binding.etMACADD.text.toString().length != 17){
            if(errors.isNotEmpty()) errors += "\r\n"
            errors += "MAC adresa nema odgovarajući broj znakova"
        }

        Log.i("addNewDeviceBt", errors)

        if(errors.isNotEmpty()){
            Log.i("addNewDeviceBt", "prikaz toasta")

            Toast.makeText(context, errors, LENGTH_SHORT).show()
            return
        }

        this.device = Device(0, binding.etName.text.toString(), binding.etChannelId.text.toString(), binding.etAPIkey.text.toString(), binding.etMACADD.text.toString(), "")

        val sameDevices = deviceRepository.getAllDevices().filter { o -> o.channelId == device.channelId && o.ReadAPIkey == device.ReadAPIkey};
        if(sameDevices.isNotEmpty()){
            Toast.makeText(context, "Postoji uređaj sa istim API ključem i Channel Id-om", LENGTH_SHORT).show()
        }
        else{


            val smallCallListener: HTTPCallFinished =
                object: HTTPCallFinished{
                    override fun callFinished(value: String?) {
                        if(value == null ){
                            Log.i("httpVV", "add new NULL")
                            return
                        }
                        Log.i("httpVV", "tjt")
                        Log.i("httpVV", value);
                        if(value.length > 30){
                            Log.i("ajdSta", "tu sam")
                            deviceRepository.save(device);
                            Toast.makeText(context, "Uspješno obavljena provjera", LENGTH_SHORT).show()
                            Log.i("fetchAll", "prije poziva")
                            DeviceDataFetcher().fetchAllandSave(device.channelId, device.ReadAPIkey)
                            Log.i("fetchAll", "nakon poziva")
                            goBack()
                        }
                        else{
                            Toast.makeText(context, "Podaci nisu uspješno provjereni", LENGTH_SHORT).show()
                        }
                    }
                }
            HTTPcalls.requestCall(DeviceDataFetcher().constructBasicUrl(device.channelId, device.ReadAPIkey), smallCallListener)
        }
    }

    private fun goBack() {
        findNavController().navigateUp()
    }

}