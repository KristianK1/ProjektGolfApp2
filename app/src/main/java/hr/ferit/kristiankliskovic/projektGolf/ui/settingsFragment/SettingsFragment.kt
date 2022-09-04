package hr.ferit.kristiankliskovic.projektGolf.ui.settingsFragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hr.ferit.kristiankliskovic.projektGolf.data.preferencesManager
import hr.ferit.kristiankliskovic.projektGolf.databinding.FragmentSettingsBinding
import hr.ferit.kristiankliskovic.projektGolf.di.DeviceRepositoryFactory
import hr.ferit.kristiankliskovic.projektGolf.model.Device
import java.text.SimpleDateFormat
import java.util.*


class SettingsFragment: Fragment(), onDeviceLongPress, onDeviceSelected {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var adapter: DeviceAdapter
    private val deviceRepository = DeviceRepositoryFactory.deviceRepository

    private val history_modes = arrayOf(
        "10 min",
        "20 min",
        "30 min",
        "1 hour",
        "2 hours",
        "3 hours",
        "6 hours",
        "12 hours",
        "1 day",
        "2 days",
        "5 days",
        "7 days",
        "10 days",
        "14 days",
        "30 days",
        "60 days",
        "90 days",
        "120 days",
        "180 days",
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        setupRecycleView()
        binding.fabB.setOnClickListener{ showAddNewFragment() }
        binding.date1.setOnClickListener{ pickDate1() }
        binding.date2.setOnClickListener { pickDate2() }
        binding.historyShow.setOnClickListener{ pickHistory() }
        this.binding.intervalChoice.setOnClickListener { choiceChanged(1) }
        this.binding.historyChoice.setOnClickListener { choiceChanged(2) }
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
        showTimestamps()
        showHistory()
        getChoice()
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
            binding.tvCurrentName.text = "Nema uređaja"
        }
        else{
            binding.tvCurrentName.text = newName
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
                        deviceRepository.deleteLSfrom(device)
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

    private fun pickDate1() {
        Log.i("pickDate", "pickDate1")
        // Get Current Date
        val c: Calendar = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), 0, (DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
            pickTime1(i,i2,i3)} ), mYear, mMonth, mDay).show()
    }

    private fun pickTime1(y: Int,m: Int,d: Int){

        val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
            object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

                    val myCall1: Calendar = Calendar.getInstance().apply {
                        set(Calendar.YEAR, y)
                        set(Calendar.MONTH, m)
                        set(Calendar.DAY_OF_MONTH, d)
                        set(Calendar.HOUR_OF_DAY, hourOfDay)
                        set(Calendar.MINUTE, minute)
                        set(Calendar.SECOND, 0)
                    }

                    val formattedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(myCall1.time)
                    Log.i("pickDate", "done "+ formattedDate)
                    setIntervalTimestamp(1, formattedDate)
                }
            }

        val timePicker = TimePickerDialog(
                requireContext(),
                timePickerDialogListener,
                0,
                0,
                true
            )
        timePicker.show()

    }

    private fun pickDate2() {
        val c: Calendar = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), 0, (DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
            pickTime2(i,i2,i3)} ), mYear, mMonth, mDay).show()
    }

    private fun pickTime2(y: Int,m: Int,d: Int) {

        val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
            object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

                    var myCall2: Calendar = Calendar.getInstance().apply {
                        set(Calendar.YEAR, y)
                        set(Calendar.MONTH, m)
                        set(Calendar.DAY_OF_MONTH, d)
                        set(Calendar.HOUR_OF_DAY, hourOfDay)
                        set(Calendar.MINUTE, minute)
                        set(Calendar.SECOND, 0)
                    }

                    val formattedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(myCall2.time)
                    Log.i("pickDate", "done "+ formattedDate)
                    setIntervalTimestamp(2, formattedDate)
                }
            }

        val timePicker: TimePickerDialog = TimePickerDialog(
            requireContext(),
            timePickerDialogListener,
            0,
            0,
            true
        )
        timePicker.show()
    }

    private fun checkDateValidity(date1: String, date2: String): Boolean {
        if(date1.isEmpty() || date2.isEmpty()) return true
        var unix1: Long = 0
        var unix2: Long = 0


        var sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

        try {
            val date = sdf.parse(date1)
            val cal = Calendar.getInstance()
            cal.time = date
            unix1 = cal.timeInMillis
        } catch (e: Throwable){
            Log.i("unix", "catch1")
        }
        try {
            val date = sdf.parse(date2)
            val cal = Calendar.getInstance()
            cal.time = date
            unix2 = cal.timeInMillis
        }catch (e: Throwable){
            Log.i("unix", "catch2")
        }
        Log.i("unix1", ""+unix1)
        Log.i("unix2", ""+unix2)
        Log.i("unixx", "" + (unix2-unix1))
        Log.i("unixx", "" + (1000L*60L*60L*24L*200L))

        if(unix2 <= unix1){
            Toast.makeText(context, "Vrijeme početka i kraja nisu konzistenti", Toast.LENGTH_SHORT).show()
            return false;
        }
        else if((unix2 - unix1) > (1000L*60L*60L*24L*200L)) {
            Toast.makeText(context, "Vrijeme početka i kraja se razlikuju za više od 200 dana", Toast.LENGTH_SHORT).show()
            return false;
        }
        return true;
    }

    private fun setIntervalTimestamp(which: Int, date: String){
        val d1: String
        val d2: String
        if(which == 1){
            d1 = date
            d2 = preferencesManager().getTimestamp(2)
        }
        else if (which == 2){
            d1 = preferencesManager().getTimestamp(1)
            d2 = date
        }
        else throw RuntimeException("wrong whichhh")

        Log.i("unix", d1)
        Log.i("unix", d2)

        Log.i("unix", "HELLO")
        if(!checkDateValidity(d1,d2)) {
            return
        }
        showTimeStamp(which, date)
        preferencesManager().saveTimestamp(which,date);
    }

    private fun showTimeStamp(which: Int, dateStr: String) {
        val cal = Calendar.getInstance()
        try{
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val date = sdf.parse(dateStr)
            cal.time = date
        } catch(e: Throwable){
            if(which == 1)
                binding.date1.text = "Enter start time"
            else if(which == 2)
                binding.date2.text = "Enter end time"
            return
        }
        if(which == 1)
            binding.date1.text = SimpleDateFormat("dd.MM.yyyy HH:mm").format(cal.time)
        else if(which == 2)
            binding.date2.text = SimpleDateFormat("dd.MM.yyyy HH:mm").format(cal.time)
        else throw (throw RuntimeException("wrong which value"))
    }

    private fun showTimestamps(){
        showTimeStamp(1, preferencesManager().getTimestamp(1))
        showTimeStamp(2, preferencesManager().getTimestamp(2))
    }

    private fun pickHistory() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Pick a color")
        builder.setItems(history_modes, DialogInterface.OnClickListener { dialog, which ->
            val chosen = history_modes[which];
            this.binding.historyShow.text = chosen
            preferencesManager().setHistory(chosen)
        })
        builder.show()
    }


    private fun showHistory() {
        var setable = preferencesManager().getHistory()
        if(setable.isEmpty() ) setable =  "Enter history"
        this.binding.historyShow.text = setable
    }

    private fun choiceChanged(which: Int) {
        preferencesManager().setChoice(which)
        printChoice(which)
    }

    private fun getChoice(){
        val cc = preferencesManager().getChoice()
        if(cc == 1 || cc == 2)
            printChoice(cc)
    }

    private fun printChoice(which: Int) {
        if(which == 1){
            this.binding.intervalChoice.isChecked = true
            this.binding.historyChoice.isChecked = false
            this.binding.intervalSideView.setBackgroundColor(Color.GREEN);
            this.binding.historySideView.setBackgroundColor(Color.RED);

        }
        if(which == 2){
            this.binding.intervalChoice.isChecked = false
            this.binding.historyChoice.isChecked = true
            this.binding.intervalSideView.setBackgroundColor(Color.RED);
            this.binding.historySideView.setBackgroundColor(Color.GREEN);
        }
    }

}

