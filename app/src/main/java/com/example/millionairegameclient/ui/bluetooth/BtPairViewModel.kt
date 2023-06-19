package com.example.millionairegameclient.ui.bluetooth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class BtPairViewModel @Inject constructor(
    private val myBtRepository: BluetoothDataRepository
): ViewModel() {
    
    companion object {
        public const val TAG = "BluetoothViewModel"
    }

    private val _btState = MutableLiveData<PairingUIState<String>?>()
    val btState = myBtRepository.btState

    private val _scanState = MutableStateFlow(BluetoothUIState())
    val scanState = myBtRepository.isScanning
    
    private val _state = MutableStateFlow(BluetoothUIState())
    val state = combine(
        myBtRepository.scannedDevices,
        myBtRepository.pairedDevices,
        myBtRepository.state
    ) { scannedDevices, pairedDevices, state ->
        state.copy(
            scannedDevices = scannedDevices,
            pairedDevices = pairedDevices,
            data = if (state.isConnected) state.data else byteArrayOf()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)

    init {
        myBtRepository.isConnected.onEach { isConnected ->
            _state.update { it.copy(isConnected = isConnected) }
        }.launchIn(viewModelScope)

        myBtRepository.errors.onEach { error ->
            _state.update { it.copy(
                errorMessage = error
            ) }
        }.launchIn(viewModelScope)

        myBtRepository.btState.onEach {
            _btState.value = it
        }.launchIn(viewModelScope)
    }

    fun connectToDevice(device: BluetoothDeviceDomain) {
        myBtRepository.connectToDevice(device)
        _state.update { it.copy(isConnecting = true) }
    }

    fun disconnectFromDevice() {
        myBtRepository.closeConnection()
    }

    /*fun sendMessage(frame: FrameData) {
        Log.d(TAG, "sendMessage: id - ${frame.id}")
        val frameArray = Encoder().encodeDataForNotification(frame)
        viewModelScope.launch {
            myBtRepository.trySendMessage(frameArray)
        }
    }*/

    fun startScan() {
        Log.d(TAG, "startScan: ")
        myBtRepository.startDiscovery()
    }

    fun stopScan() {
        Log.d(TAG, "stopScan: ")
        myBtRepository.stopDiscovery()
    }

    override fun onCleared() {
        super.onCleared()
        myBtRepository.release()
    }
}