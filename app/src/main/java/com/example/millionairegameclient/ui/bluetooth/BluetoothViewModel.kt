package com.example.millionairegameclient.ui.bluetooth

import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BluetoothViewModel @Inject constructor(
    private val dataRepository: BluetoothDataRepository
): ViewModel() {

    companion object {
        private const val TAG = "BluetoothViewModel"
    }

    private val _btState = MutableLiveData<PairingUIState<String>?>()
    val btState = dataRepository.btState

    private val _scanState = MutableStateFlow(BluetoothUIState())
    val scanState = dataRepository.isScanning

    private val _state = MutableStateFlow(BluetoothUIState())
    val state = combine(
        dataRepository.pairedDevices,
        _state
    ) { pairedDevices, state ->
        state.copy(
            pairedDevices = pairedDevices,
            data = if (state.isConnected) state.data else byteArrayOf()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)

    init {
        dataRepository.isConnected.onEach { isConnected ->
            _state.update { it.copy(isConnected = isConnected) }
        }.launchIn(viewModelScope)

        dataRepository.errors.onEach { error ->
            _state.update { it.copy(
                errorMessage = error
            ) }
        }.launchIn(viewModelScope)

        dataRepository.btState.onEach {
            _btState.value = it
        }.launchIn(viewModelScope)
    }

    fun connectToDevice(device: BluetoothDevice) {
        Log.d(TAG, "connectToDevice: ")
        dataRepository.connectToDevice(device)
        //_state.update { it.copy(isConnecting = true) }
    }

    fun test() {
        Log.d(TAG, "test: ")
        dataRepository.test()
    }

    fun disconnectFromDevice() {
        dataRepository.closeConnection()
    }

    fun sendMessage(data: ByteArray) {
        Log.d(TAG, "sendMessage: ")
        viewModelScope.launch {
            dataRepository.trySendMessage(data)
        }
    }

    fun startScan() {
        Log.d(TAG, "startScan: ")
        dataRepository.startDiscovery()
    }

    fun stopScan() {
        Log.d(TAG, "stopScan: ")
        dataRepository.stopDiscovery()
    }

    override fun onCleared() {
        super.onCleared()
        dataRepository.release()
    }
}