package com.example.millionairegameclient.ui.lifelines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LifelinesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is lifelines Fragment"
    }
    val text: LiveData<String> = _text
}