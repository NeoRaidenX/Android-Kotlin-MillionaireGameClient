package com.example.millionairegameclient.ui.lifelines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.millionairegameclient.ui.bluetooth.BluetoothDataRepository
import com.example.millionairegameclient.ui.settings.SettingsEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LifelinesViewModel @Inject constructor(
    private val dataRepository: BluetoothDataRepository
): ViewModel() {

    companion object {
        private const val TAG = "SettingsViewModel"
    }

    fun sendAction(option: LifelinesEnum) {
        dataRepository.sendLifelineAction(option)
    }
}