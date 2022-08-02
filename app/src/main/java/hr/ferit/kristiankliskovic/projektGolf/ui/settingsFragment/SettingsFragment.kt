package hr.ferit.kristiankliskovic.projektGolf.ui.settingsFragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hr.ferit.kristiankliskovic.projektGolf.data.preferencesManager
import hr.ferit.kristiankliskovic.projektGolf.databinding.FragmentSettingsBinding
import hr.ferit.kristiankliskovic.projektGolf.di.DeviceRepositoryFactory
import hr.ferit.kristiankliskovic.projektGolf.mainSomething
import hr.ferit.kristiankliskovic.projektGolf.model.Device
import hr.ferit.kristiankliskovic.projektGolf.utils.HTTPcalls


class SettingsFragment: Fragment(), onDeviceLongPress, onDeviceSelected {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var adapter: DeviceAdapter
    private val deviceRepository = DeviceRepositoryFactory.deviceRepository


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        setupRecycleView()
        binding.btAddNew.setOnClickListener{ showAddNewFragment() }
        return binding.root
    }

    private fun setupRecycleView() {
        binding.rvDevices.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        adapter = DeviceAdapter()
        adapter.onDeviceSelectedListener = this
        adapter.onDeviceLongPressListener = this
        binding.rvDevices.adapter = adapter
    }


    override fun onResume() {
        super.onResume()
        updateData()
        HTTPcalls.requestCall()
    }

    fun getCurrentDevice(): String? {
        val currDevName = preferencesManager().getCurrDeviceName()
        val allDevices = deviceRepository.getAllDevices()
        if(allDevices.filter { o -> o.name == currDevName }.isNotEmpty()) return currDevName
        return null
    }

    fun setCurrentDevice(newName: String){
        preferencesManager().saveCurrDeviceName(newName)
        if(newName.isEmpty()){
            binding.tvCurrentName.text = "Nema trenutnog uređaja";
        }
        else{
            binding.tvCurrentName.text = newName;
        }
    }

    private fun updateData(){
        val allDevs = deviceRepository.getAllDevices()
        adapter.setDevices(allDevs)

        val currDev = getCurrentDevice()
        if(currDev == null){
            if(allDevs.isNotEmpty()){
                setCurrentDevice(allDevs[0].name)
            }
            else{
                setCurrentDevice("")
            }
        }
        else{
            binding.tvCurrentName.text = currDev
        }
    }

    private fun showAddNewFragment() {
        val action = SettingsFragmentDirections.actionSettingsFragmentToAddNewDeviceFragment()
        findNavController().navigate(action)
    }

    override fun onDeviceLongPress(device: Device) {
        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        deviceRepository.deviceDao.delete(device)
                        updateData()
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {}
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMessage("Delete device?").setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("No", dialogClickListener).show()
    }

    override fun onDeviceSelected(device: Device) {
        setCurrentDevice(device.name)
    }
}