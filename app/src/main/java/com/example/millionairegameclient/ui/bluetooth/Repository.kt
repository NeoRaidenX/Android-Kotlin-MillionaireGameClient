package com.example.millionairegameclient.ui.bluetooth

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface Repository {
    val isConnected: StateFlow<Boolean>
    val state: StateFlow<BluetoothUIState>
    val isConnecting: StateFlow<Boolean>
    val isTransferSucceded: StateFlow<Boolean>
    val scannedDevices: StateFlow<List<BluetoothDevice>>
    val pairedDevices: StateFlow<List<BluetoothDevice>>
    val errors: SharedFlow<String>
    val isScanning: StateFlow<BluetoothScanUIState>

    fun startDiscovery()
    fun stopDiscovery()

    //fun startBluetoothServer(): Flow<BtBindingResult>
    //fun connectToDevice(device: BluetoothDevice): Flow<BtBindingResult>

    fun connectToDevice(device: BluetoothDevice)
    suspend fun trySendMessage(data: ByteArray)

    fun closeConnection()
    fun release()
}