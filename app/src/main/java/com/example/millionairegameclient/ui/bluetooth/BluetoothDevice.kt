package com.example.millionairegameclient.ui.bluetooth

typealias BluetoothDeviceDomain = BluetoothDevice

data class BluetoothDevice(
    val name: String,
    val address: String
)