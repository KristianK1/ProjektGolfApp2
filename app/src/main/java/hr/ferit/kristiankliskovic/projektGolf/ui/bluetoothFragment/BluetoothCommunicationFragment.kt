package hr.ferit.kristiankliskovic.projektGolf.ui.bluetoothFragment

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import hr.ferit.kristiankliskovic.projektGolf.data.preferencesManager
import hr.ferit.kristiankliskovic.projektGolf.databinding.BluetoothScreenBinding
import hr.ferit.kristiankliskovic.projektGolf.di.DeviceRepositoryFactory
import java.util.*

class BluetoothCommunicationFragment: Fragment() {

    private var bluetoothManager: BluetoothManager? = null
    private var bluetoothAdapter: BluetoothAdapter? = null

    private lateinit var binding: BluetoothScreenBinding
    private val deviceRepository = DeviceRepositoryFactory.deviceRepository
    private var BTadress: String? = null
    private var deviceHardwareAddress: String = "";


    private var deviceName = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BluetoothScreenBinding.inflate(layoutInflater)

        bluetoothManager = getSystemService(requireContext(), BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager?.getAdapter()
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        }


        if (bluetoothAdapter?.isEnabled == false) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
            }
            bluetoothAdapter?.enable()
//            connect()
        }

        binding.sendBTT1.setOnClickListener {

        }

        binding.sendBTT2.setOnClickListener {

        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        findBTadress()
        connect()
    }

    private fun findBTadress() {
        val Name = preferencesManager().getCurrDeviceName()
        val devices = deviceRepository.deviceDao.getAllDevices()
        val curr = devices.find { o -> o.name == Name }
        BTadress = curr?.BT_MAC_address
    }

    private fun connect() {
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        pairedDevices?.forEach { device ->
            deviceName = device.name
            deviceHardwareAddress = device.address // MAC address
            if (deviceHardwareAddress == BTadress) {
                val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
                    bluetoothAdapter?.listenUsingInsecureRfcommWithServiceRecord(
                        "APPLICATION",
                        UUID.fromString("1ac263bd-59f7-456a-a3be-c3480c1db824")
                    )
                }

                mmServerSocket?.let{ socket ->
                    val ss = socket.accept()
                    ss.connect()
                }
            }
        }
    }

    private fun send() {

    }

}

