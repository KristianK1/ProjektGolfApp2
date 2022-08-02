package hr.ferit.kristiankliskovic.projektGolf.ui.addNewDeviceFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.*
import androidx.fragment.app.Fragment
import hr.ferit.kristiankliskovic.projektGolf.databinding.FragmentAddNewDeviceBinding
import hr.ferit.kristiankliskovic.projektGolf.di.DeviceRepositoryFactory
import hr.ferit.kristiankliskovic.projektGolf.model.Device

class AddNewDeviceFragment: Fragment() {

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
/*        Log.i("addNewDeviceBt", "ulazak u fun")
        var errors: String = "";
        if(binding.etName.text.toString().length < 4){
            errors += "Ime mora sadržavati najmanje 4 znaka"
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
        }*/

        var device = Device(0, binding.etName.text.toString(), binding.etChannelId.text.toString(), binding.etAPIkey.text.toString(), binding.etMACADD.text.toString())

        val sameDevices = deviceRepository.getAllDevices().filter { o -> o.channelId == device.channelId && o.ReadAPIkey == device.ReadAPIkey};
        if(sameDevices.isNotEmpty()){
            Toast.makeText(context, "Postoji uređaj sa istim API ključem i Channel Id-om", LENGTH_SHORT).show()
        }
        else{
            deviceRepository.save(device)
        }
    }


}