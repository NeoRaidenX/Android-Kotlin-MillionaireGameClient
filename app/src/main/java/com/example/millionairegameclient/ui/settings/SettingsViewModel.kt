package com.example.millionairegameclient.ui.settings

import androidx.lifecycle.ViewModel
import com.example.millionairegameclient.ui.bluetooth.BluetoothDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataRepository: BluetoothDataRepository
): ViewModel() {

    companion object {
        private const val TAG = "SettingsViewModel"
    }

    fun sendAction(option: SettingsEnum) {
        dataRepository.sendSettingsAction(option)
    }

    fun sendCurrent(position: Int) {
        dataRepository.sendCurrent(position)
    }
}