package com.example.millionairegameclient.ui.bluetooth

import android.app.Service
import android.content.Intent
import android.os.IBinder

class BluetoothService: Service() {

    companion object {
        val UUID = "4719b673-6d6a-4bad-8772-5a603a7de243"
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}