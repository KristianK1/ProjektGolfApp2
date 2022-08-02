package hr.ferit.kristiankliskovic.projektGolf.ui.settingsFragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.ferit.kristiankliskovic.projektGolf.R
import hr.ferit.kristiankliskovic.projektGolf.databinding.ItemDeviceBinding
import hr.ferit.kristiankliskovic.projektGolf.model.Device

class DeviceAdapter: RecyclerView.Adapter<DeviceViewHolder>() {
    val devices = mutableListOf<Device>()
    var onDeviceSelectedListener: onDeviceSelected? = null
    var onDeviceLongPressListener: onDeviceLongPress? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_device, parent,false)
        return DeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = devices[position]
        holder.bind(device)

        onDeviceSelectedListener?.let { listener ->
            holder.itemView.setOnClickListener { listener.onDeviceSelected(device) }
        }

        onDeviceLongPressListener?.let { listener ->
            holder.itemView.setOnLongClickListener { listener.onDeviceLongPress(device) == Unit } //ne znam
        }
    }

    override fun getItemCount(): Int {
        Log.i("Hello", "Prije neceg tamo");
        return devices?.count()
    }

    fun setDevices(devices: List<Device>){
        this.devices.clear()
        this.devices.addAll(devices)
        this.notifyDataSetChanged()
    }

}


class DeviceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    fun bind(device: Device){
        val binding = ItemDeviceBinding.bind(itemView)
        binding.devName.text = device.name
    }
}