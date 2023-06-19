package com.example.millionairegameclient.ui.bluetooth

sealed interface ConnectionResult {
    object ConnectionEstablished: ConnectionResult
    data class TransferSucceeded(val data: ByteArray): ConnectionResult
    data class Error(val message: String): ConnectionResult
}