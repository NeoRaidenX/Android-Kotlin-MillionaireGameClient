package com.example.millionairegameclient.ui.bluetooth

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.bluetooth.BluetoothDevice
import com.example.millionairegameclient.R

class BtDeviceAdapter(private val onClick: (BluetoothDevice) -> Unit) :
    ListAdapter<BluetoothDevice, BtDeviceAdapter.ViewHolder>(DeviceDiffCallback) {

    var devices: List<BluetoothDevice> = emptyList()

    class ViewHolder(view: View, val onClick: (BluetoothDevice) -> Unit) : RecyclerView.ViewHolder(view) {
        val deviceName: TextView
        private var selectedDevice: BluetoothDevice? = null

        init {
            deviceName = view.findViewById(R.id.tv_bt_device_name)
            view.setOnClickListener {
                selectedDevice?.let {
                    onClick(it)
                }
            }
        }

        @SuppressLint("MissingPermission")
        fun bind(device: BluetoothDevice) {
            selectedDevice = device
            deviceName.text = device.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_bt_device, parent, false)

        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(device = devices[position])
    }

    override fun getItemCount(): Int = devices.size

    fun updateDevices(update: List<android.bluetooth.BluetoothDevice>) {
        devices = update
        notifyDataSetChanged()
    }

}

object DeviceDiffCallback : DiffUtil.ItemCallback<BluetoothDevice>() {
    override fun areItemsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean {
        return oldItem.address == newItem.address
    }
}