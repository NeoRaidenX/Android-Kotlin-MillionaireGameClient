package com.example.millionairegameclient.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.millionairegameclient.ui.bluetooth.BluetoothDataRepository
import com.example.millionairegameclient.ui.settings.SettingsEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataRepository: BluetoothDataRepository
): ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    fun sendAction(option: MainOptionsEnum) {
        dataRepository.sendMainAction(option)
    }

    fun sendShowOption(position: Int) {
        dataRepository.sendMainShowOption(position)
    }

    fun sendMarkOption(position: Int) {
        dataRepository.sendMainMarkOption(position)
    }


}