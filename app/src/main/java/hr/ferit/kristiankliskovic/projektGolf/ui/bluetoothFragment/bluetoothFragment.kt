package hr.ferit.kristiankliskovic.projektGolf.ui.bluetoothFragment

import android.Manifest
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import hr.ferit.kristiankliskovic.projektGolf.databinding.BluetoothscreenBinding
import hr.ferit.kristiankliskovic.projektGolf.mainSomething
import hr.ferit.kristiankliskovic.projektGolf.ui.MainActivity
import hr.ferit.kristiankliskovic.projektGolf.utils.myUserState
import java.util.*

class bluetoothFragment : Fragment() {
    private lateinit var binding: BluetoothscreenBinding

    private val Bluetooth_name = "ProjectGolf"
    private var Bluetooth_uuid: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = BluetoothscreenBinding.inflate(layoutInflater)
        requestPermission(arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
            MainActivity.BLUETOOTH_PERMISSION_CODE)

        binding.sendBTT1.setOnClickListener {
            if (Bluetooth_uuid != null){
                BTsend("T=111")
            } else {
                Toast.makeText(context, "Select a device first", Toast.LENGTH_SHORT).show()
            }
        }
        binding.otherCommands.setOnClickListener {
            if (Bluetooth_uuid != null) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Pick a command")
                builder.setItems(availableCommands,
                    DialogInterface.OnClickListener { dialog, which ->
                        val chosen = availableCommands[which];
                        BTsend(availableCommands[which]);
                    })
                builder.show()
            } else {
                Toast.makeText(context, "Select a device first", Toast.LENGTH_SHORT).show()
            }
        }
        binding.sendBTT2x.setOnClickListener {
            if (Bluetooth_uuid != null) {
                BTsend("T=222,00x")
            } else {
                Toast.makeText(context, "Select a device first", Toast.LENGTH_SHORT).show()
            }
        }

        Bluetooth_uuid = myUserState.myDevice?.bt_MAC_address;
        return binding.root
    }

    fun requestPermission(permissions: Array<String>, requestCode: Int) {
        Log.i("perms", "here i am")
        if (ContextCompat.checkSelfPermission(MainActivity.activity,
                permissions[0]) == PackageManager.PERMISSION_DENIED
        ) {
            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.activity, permissions, requestCode)
        }
    }

    fun BTsend(send: String) {
        Log.i("btFind", "hello");

        try {
            var bt_adresa: String
//        val btManager: BluetoothManager? = getSystemService(BluetoothManager::class.java)
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            Log.i("btFind", "" + Bluetooth_uuid);
            val myDevice = bluetoothAdapter.getRemoteDevice(Bluetooth_uuid)
            Log.i("btFind", "here4");

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.BLUETOOTH
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                val should = ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.BLUETOOTH
                )
                Log.i("btFind", "should " + should)
                Log.i("btFind", "code")
                Log.i("btFindmyACT", requireActivity().toString())
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.BLUETOOTH),
                    1001
                );

                Log.i("btFind", "denied");
                return
            }
            Log.i("btFind", myDevice.name)
            val x = myDevice.createBond()
            Log.i("btFind_bond", "" + x);




            Log.i("btsend_uuids", "" + myDevice.uuids.size)
            myDevice.uuids.size
            Log.i("btsend", "poslao?")
            Log.i("btsend", myDevice.address)

            val socket = myDevice.createRfcommSocketToServiceRecord(myDevice.uuids[0].uuid)
            Log.i("btsend", "poslao? PRIJE CONNECT")
            socket.connect()
            Log.i("btsend", "poslao?  POSLIJE CONNECT")

            val outputStream = socket.outputStream
            Log.i("btsend", "poslao?")
            val inStream = socket.inputStream
            Log.i("btsend", "poslao?")
            outputStream.write(send.toByteArray())
            Log.i("btsend", "poslao?")
            val time1: Long = current_miliseconds()
            var reply = ""
            Log.i("btsend", "poslao?2")
            Log.i("btsendunix", "" + time1)
            while (current_miliseconds() - time1 < 3000) {  //PONOC fix this
                while (inStream.available() > 0) reply += inStream.read().toChar()
                if (reply.contains("T=111")) break
                if (reply.contains("T=222,") && reply.length == 12) break
                if (reply.contains("Error")) break
            }
            socket.close()
            var print = reply;
            if (print.length === 0) print = "Bluetooth comm failed"
            Toast.makeText(context, print, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Bluetooth comm failed",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun current_miliseconds(): Long {
        val cal = Calendar.getInstance()
        return cal.timeInMillis
    }


    private val availableCommands = arrayOf(
        "T=222,001",
        "T=222,002",
        "T=222,005",
        "T=222,010",
        "T=222,020",
        "T=222,030",
        "T=222,060",
        "T=222,120",
        "T=222,360",
        "BATT_EMPTY",
        "RESET_DEVICE"
    );
}