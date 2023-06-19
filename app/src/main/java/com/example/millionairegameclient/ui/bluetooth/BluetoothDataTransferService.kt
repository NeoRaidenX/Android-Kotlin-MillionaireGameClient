package com.example.millionairegameclient.ui.bluetooth

import android.bluetooth.BluetoothSocket
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException

class BluetoothDataTransferService(
    private val socket: BluetoothSocket
) {
    private val TAG = "BtTransferService"


    suspend fun sendMessage(bytes: ByteArray): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                socket.outputStream.write(bytes)
            } catch(e: IOException) {
                e.printStackTrace()
                return@withContext false
            }

            true
        }
    }

    fun listenForIncomingMessages(): Flow<ByteArray> {
        return flow {
            if(!socket.isConnected) {
                Log.d(TAG, "socket not connected: ")
                return@flow
            }
            val buffer = ByteArray(2048)
            while(true) {
                val byteCount = try {
                    socket.inputStream.read(buffer)
                } catch(e: IOException) {
                    Log.e(TAG, "listenForIncomingMessages: ", e)
                    throw TransferFailedException()
                }

                if (byteCount > 0) {
                    emit(
                        buffer
                    )
                }
            }
        }.flowOn(Dispatchers.IO)
    }
}