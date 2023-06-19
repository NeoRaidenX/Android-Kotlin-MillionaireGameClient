package com.example.millionairegameclient.ui.bluetooth

sealed interface BtBindingResult {
    object ConnectionEstablished: BtBindingResult
    data class DataReceived(val message: ByteArray): BtBindingResult
    data class Error(val message: String): BtBindingResult
}