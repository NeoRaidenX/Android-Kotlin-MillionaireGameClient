package com.example.millionairegameclient.ui.bluetooth

data class BluetoothUIState(
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val pairedDevices: List<BluetoothDevice> = emptyList(),
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,
    val isTransferSucceeded: Boolean = false,
    val isRegisterModuleNotified: Boolean = false,
    val isRegisterModuleConfirmed: Boolean = false,
    val isPermissionRequest: Boolean = false,
    val isPermissionResponded: Boolean = false,
    val data: ByteArray = byteArrayOf()
)
