package com.example.millionairegameclient.ui.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.os.ParcelUuid
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.millionairegameclient.MainActivity
import com.example.millionairegameclient.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
@SuppressLint("MissingPermission")
@AndroidEntryPoint
class BluetoothService: Service() {

    companion object {
        private const val TAG = "BluetoothService"
        const val SERVICE_UUID = "0000112f-0000-1000-8000-00805f9b34fb"
    }

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private var deviceConnectionJob: Job? = null
    private var currentClientSocket: BluetoothSocket? = null
    private var dataTransferService: BluetoothDataTransferService? = null
    private val bluetoothManager by lazy {
        getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    @Inject
    lateinit var myBtRepository: BluetoothDataRepository

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ${intent?.action}")
        when (intent?.action) {
            Actions.START_FOREGROUND -> {
                startForegroundService()
            }
            Actions.MAIN_SHOW_QUESTION -> {

            }
            Actions.MAIN_SHOW_ANSWER -> {

            }
            Actions.MAIN_SHOW_REWARD -> {

            }
            Actions.MAIN_CHANGE_NEXT_Q -> {

            }
            Actions.LIFE_SHOW_PPL_FORM -> {

            }
            Actions.LIFE_SHOW_PPL_CHOICE -> {

            }
            Actions.LIFE_SHOW_CLOCK -> {

            }
            Actions.LIFE_SHOW_50 -> {

            }
            Actions.LIFE_TOGGLE_FORM -> {

            }
            Actions.LIFE_TOGGLE_CHART -> {

            }
            Actions.LIFE_TOGGLE_TIMER -> {

            }
            Actions.LIFE_TOGGLE_50 -> {

            }
            Actions.CONFIG_SHOW_OPENING -> {

            }
            Actions.CONFIG_SHOW_TABLE -> {

            }
            Actions.CONFIG_SELECT_QUEST -> {

            }
            Actions.SEND_ACTION -> {
                val data: ByteArray? = intent.getByteArrayExtra("data")
                Log.d(TAG, "SEND_ACTION: ")
                scope.launch {
                    data?.let {
                        sendMessage(it)
                    }
                }
            }
        }
        return START_STICKY
    }

    private fun startForegroundService() {
        Log.d(TAG, "startForegroundService: ")
        startForeground()
    }

    private fun createNotificationChannel(): String{
        val channelId = "my_service"
        val channelName = "Bluetooth Service"
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_HIGH)
        chan.lightColor = Color.BLUE
        chan.importance = NotificationManager.IMPORTANCE_NONE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    private fun startForeground() {
        val channelId =
            createNotificationChannel()

        val notificationBuilder = NotificationCompat.Builder(this, channelId )
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0,notificationIntent,
            PendingIntent.FLAG_MUTABLE
        )
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Bluetooth is running in background")
            .setContentText("Tap to open")
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
        connectDevice(myBtRepository.deviceToConnect)
    }

    @SuppressLint("MissingPermission")
    private fun connectDevice(device: BluetoothDeviceDomain?) {
        Log.d(TAG, "connectDevice: ")
        // update the status
        broadcastUpdate(Actions.STATUS_MSG, "Connecting to ${device?.address}")
        deviceConnectionJob = flow {

            currentClientSocket = bluetoothAdapter
                ?.getRemoteDevice(device?.address)
                ?.createInsecureRfcommSocketToServiceRecord(
                    ParcelUuid.fromString(SERVICE_UUID).uuid
                )

            currentClientSocket?.let { socket ->
                try {
                    socket.connect()
                    //emit(BtBindingResult.ConnectionEstablished)
                    BluetoothDataTransferService(socket).also {
                        dataTransferService = it
                        /*emitAll(
                            it.listenForIncomingMessages()
                                .map { msg ->
                                    BtBindingResult.DataReceived(msg)
                                }
                        )*/
                    }

                } catch (e: IOException) {
                    Log.e(TAG, "connectToDevice: ", e)
                    Log.d(TAG, "connectDevice: ${e.message}" )
                    socket.close()
                    currentClientSocket = null
                    emit(BtBindingResult.Error("Conexión interrumpida " + e.message))
                    disconnectFromDevice()
                }
            }
        }.onCompletion {
            closeConnection()
        }.flowOn(Dispatchers.IO).listen()
    }

    private fun Flow<BtBindingResult>.listen(): Job {
        return onEach { result ->
            Log.d(TAG, "listen: name - $result")
        }
            .catch { throwable ->
                Log.d(BtPairViewModel.TAG, "listen: catch" + throwable.stackTraceToString())
                myBtRepository.closeConnection()
            }
            .launchIn(scope)
    }

    private fun closeConnection() {
        currentClientSocket?.close()
        currentClientSocket = null
    }

    private fun disconnectFromDevice() {
        Log.d(TAG, "disconnectFromDevice: ")
        deviceConnectionJob?.cancel()
        currentClientSocket?.close()
        currentClientSocket = null
        stopForegroundService()
    }

    private fun stopForegroundService() {
        stopForeground(true)
        stopSelf()
    }

    private fun broadcastUpdate(action: String, msg: String) {
        Log.d(TAG, "broadcastUpdate: $action msg: $msg")
        val intent = Intent(action)
        intent.putExtra(Actions.MSG_DATA, msg)
        sendBroadcast(intent)
    }

    private suspend fun sendMessage(data: ByteArray) {
        Log.d(TAG, "sendMessage: ")
        if(!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            Log.d(BluetoothDataRepository.TAG, "trySendMessage: no permission")
            return
        }

        if(dataTransferService == null) {
            Log.d(BluetoothDataRepository.TAG, "trySendMessage: dataTransferService null")
            return
        }

        val sended: Boolean? = dataTransferService?.sendMessage(data)
        if (sended == true) Log.d(TAG, "message sended: ")
    }

    private fun hasPermission(permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        return true
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}