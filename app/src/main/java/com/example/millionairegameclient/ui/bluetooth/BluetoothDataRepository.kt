package com.example.millionairegameclient.ui.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.millionairegameclient.App
import com.example.millionairegameclient.ui.home.MainOptionsEnum
import com.example.millionairegameclient.ui.lifelines.LifelinesEnum
import com.example.millionairegameclient.ui.settings.SettingsEnum
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.FieldPosition
import java.util.*


@SuppressLint("MissingPermission")
class BluetoothDataRepository(
    private val context: Context
): Repository {

    companion object {
        const val TAG = "BluetoothDataRepository"
        //const val SERVICE_UUID = "00001101-0000-1000-8000-00805F9B34FB"
    }

    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    var deviceToConnect: BluetoothDeviceDomain? = null
    var outputData: ByteArray? = null

    private val _btState = MutableStateFlow<PairingUIState<String>>(PairingUIState.Loading("Million"))
        val btState: StateFlow<PairingUIState<String>>
        get() = _btState.asStateFlow()


    private val _state = MutableStateFlow(BluetoothUIState())
    override val state: StateFlow<BluetoothUIState>
        get() = _state.asStateFlow()

    private val _isConnected = MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean>
        get() = _isConnected.asStateFlow()

    private val _isConnecting = MutableStateFlow(false)
    override val isConnecting: StateFlow<Boolean>
        get() = _isConnecting.asStateFlow()

    private val _isTransferSucceded = MutableStateFlow(false)
    override val isTransferSucceded: StateFlow<Boolean>
        get() = _isTransferSucceded.asStateFlow()

    private val _scannedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    override val scannedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _scannedDevices.asStateFlow()

    private val _pairedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    override val pairedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _pairedDevices.asStateFlow()

    private val _errors = MutableSharedFlow<String>()
    override val errors: SharedFlow<String>
        get() = _errors.asSharedFlow()

    private val _scanState = MutableStateFlow(BluetoothScanUIState())
    override val isScanning: StateFlow<BluetoothScanUIState> = _scanState


    private val foundDeviceReceiver = FoundDeviceReceiver { device ->
        Log.d(TAG, "device found: ")
        _scannedDevices.update { devices ->
            Log.d(TAG, "device update: ")
            val newDevice = device.toBluetoothDeviceDomain()
            if (newDevice in devices) devices else devices + newDevice
        }
    }

    private var mBtUpdateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d(TAG,"action ${intent.action}")
            when(intent.action){
                Actions.BT_CONNECTED-> {
                    Log.d(TAG, "onReceive: BT_CONNECTED")
                    _state.update { it.copy(
                        isConnected = true,
                        isConnecting = false,
                        errorMessage = null
                    ) }
                    CoroutineScope(Dispatchers.IO).launch {
                        _btState.update { PairingUIState.Success }
                    }
                }
                Actions.BT_DISCONNECTED->{

                }
                Actions.STATUS_MSG -> {
                    Log.d(TAG, "onReceive: ")
                }
                Actions.ERROR -> {
                    Log.d(TAG, "onReceive: error")
                    val msg = intent.getStringExtra(Actions.MSG_DATA)?:"no message"
                    _state.update { it.copy(
                        isConnected = false,
                        isConnecting = false,
                        errorMessage = msg
                    ) }
                    CoroutineScope(Dispatchers.IO).launch {
                        _btState.emit(PairingUIState.Error(msg))
                    }

                    //_btState.update { PairingUIState.Error(R.string.app_name) }
                }

            }

        }
    }

    private val bluetoothStateReceiver = BluetoothStateReceiver { isConnected, bluetoothDevice ->
        if(bluetoothAdapter?.bondedDevices?.contains(bluetoothDevice) == true) {
            _isConnected.update { isConnected }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                _errors.emit("Can't connect to a non-paired device")
            }
        }
    }



    private var connected: Boolean = false
    private lateinit var  mConnectedDeviceName: String
    private var mChatService: BluetoothChatService? = null

    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {

            when (msg.what) {

                Constants.MESSAGE_STATE_CHANGE -> {
                    Log.d(TAG, "MESSAGE_STATE_CHANGE: ")
                    when (msg.arg1) {

                        BluetoothChatService.STATE_CONNECTED -> {
                            Log.d(TAG, "STATE_CONNECTED: ")
                            connected = true
                        }

                        BluetoothChatService.STATE_CONNECTING -> {
                            Log.d(TAG, "STATE_CONNECTING: ")
                            connected = false
                        }

                        BluetoothChatService.STATE_LISTEN, BluetoothChatService.STATE_NONE -> {
                            Log.d(TAG, "STATE_LISTEN: ")
                            connected = false
                        }
                    }
                }

                Constants.MESSAGE_WRITE -> {
                    Log.d(TAG, "MESSAGE_WRITE: ")
                    val writeBuf = msg.obj as ByteArray
                    // construct a string from the buffer
                    val writeMessage = String(writeBuf)

                }
                Constants.MESSAGE_READ -> {
                    Log.d(TAG, "MESSAGE_READ: ")
                    val readBuf = msg.obj as ByteArray
                    // construct a string from the valid bytes in the buffer
                    val readMessage = String(readBuf, 0, msg.arg1)
                }
                Constants.MESSAGE_DEVICE_NAME -> {
                    Log.d(TAG, "MESSAGE_DEVICE_NAME: ")
                    // save the connected device's name
                    mConnectedDeviceName = msg.data.getString(Constants.DEVICE_NAME)!!
                    connected = true
                    Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show()
                }
                Constants.MESSAGE_TOAST -> {
                    Log.d(TAG, "MESSAGE_TOAST: ")
                    connected = false
                }
            }
        }
    }







    init {
        updatePairedDevices()
        context.registerReceiver(
            bluetoothStateReceiver,
            IntentFilter().apply {
                addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
                addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
                addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            }
        )
        context.registerReceiver(
            mBtUpdateReceiver,
            IntentFilter().apply {
                addAction(Actions.BT_CONNECTED)
                addAction(Actions.BT_DISCONNECTED)
                addAction(Actions.STATUS_MSG)
                addAction(Actions.ERROR)
            }
        )
    }

    override fun startDiscovery() {
        if(!hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
            Log.d(TAG, "startDiscovery: no permission")
            return
        }

        _pairedDevices.update { emptyList() }
        _scannedDevices.update { emptyList() }
        _scanState.update { it.copy( isScanning = true) }

        context.registerReceiver(
            foundDeviceReceiver,
            IntentFilter(BluetoothDevice.ACTION_FOUND)
        )

        updatePairedDevices()

        Log.d(TAG, "startDiscovery: ")
        bluetoothAdapter?.startDiscovery()
    }

    override fun stopDiscovery() {
        if(!hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
            return
        }
        _scanState.update { it.copy( isScanning = false) }
        Log.d(TAG, "stopDiscovery: ")
        bluetoothAdapter?.cancelDiscovery()
    }

    override fun closeConnection() {
        Log.d(TAG, "closeConnection: ")
        Intent(App.applicationContext(), BluetoothService::class.java).also { intent ->
            intent.action = Actions.CLOSE_CONNECTION
            App.applicationContext().startForegroundService(intent)
        }
    }

    override fun release() {
        context.unregisterReceiver(foundDeviceReceiver)
        context.unregisterReceiver(bluetoothStateReceiver)
        closeConnection()
    }

    override fun connectToDevice(device: com.example.millionairegameclient.ui.bluetooth.BluetoothDevice) {
        Log.d(TAG, "connectToDevice: ")
        deviceToConnect = device
        startForegroundService()
    }

    override suspend fun trySendMessage(data: ByteArray) {
        outputData = data
        Intent(App.applicationContext(), BluetoothService::class.java).also { intent ->
            intent.action = Actions.SEND_ACTION
            intent.putExtra("data", data)
            App.applicationContext().startForegroundService(intent)
        }
    }

    private fun startForegroundService(){
        Intent(App.applicationContext(), BluetoothService::class.java).also { intent ->
            intent.action = Actions.START_FOREGROUND
            App.applicationContext().startForegroundService(intent)
        }
    }

    private fun updatePairedDevices() {
        bluetoothAdapter
            ?.bondedDevices
            ?.map { it.toBluetoothDeviceDomain() }
            ?.also { devices -> _pairedDevices.update { devices } }
    }

    private fun hasPermission(permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        return true
    }

    fun connectToDevice(device: BluetoothDevice) {
        Log.d(TAG, "connectToDevice: ")
        mChatService = BluetoothChatService(context, mHandler)
        mChatService!!.connect(device, false)
    }

    fun test() {
        Log.d(TAG, "test: ")
        mChatService!!.write("data".toByteArray())
    }

    fun sendMainAction(option: MainOptionsEnum) {
        when (option) {
            /*MainOptionsEnum.LoadQuestion -> {
                sendAction(Actions.MAIN_LOAD_QUESTION)
            }*/
            MainOptionsEnum.ShowQuestion -> {
                sendAction(Actions.MAIN_SHOW_QUESTION)
            }
            MainOptionsEnum.ShowAnswer -> {
                sendAction(Actions.MAIN_SHOW_ANSWER)
            }
            MainOptionsEnum.ShowAllOptions -> {
                sendAction(Actions.MAIN_SHOW_ALL_OPTIONS)
            }
            MainOptionsEnum.NavigateReward -> {
                sendAction(Actions.NAVIGATE_REWARD)
            }
            MainOptionsEnum.NavigateUp -> {
                sendAction(Actions.NAVIGATE_UP)
            }
            MainOptionsEnum.NavigateClock -> {
                sendAction(Actions.NAVIGATE_CLOCK)
            }
            MainOptionsEnum.NavigateChart -> {
                sendAction(Actions.NAVIGATE_CHART)
            }
            MainOptionsEnum.NavigateTable -> {
                sendAction(Actions.NAVIGATE_TABLE)
            }
            MainOptionsEnum.NavigateNext -> {
                sendAction(Actions.NAVIGATE_NEXT)
            }
            else -> {}
        }
    }

    fun sendMainShowOption(position: Int) {
        when(position) {
            0 -> sendAction(Actions.MAIN_SHOW_OPTION_A)
            1 -> sendAction(Actions.MAIN_SHOW_OPTION_B)
            2 -> sendAction(Actions.MAIN_SHOW_OPTION_C)
            3 -> sendAction(Actions.MAIN_SHOW_OPTION_D)
        }
    }

    fun sendMainMarkOption(position: Int) {
        when(position) {
            0 -> sendAction(Actions.MAIN_MARK_OPTION_A)
            1 -> sendAction(Actions.MAIN_MARK_OPTION_B)
            2 -> sendAction(Actions.MAIN_MARK_OPTION_C)
            3 -> sendAction(Actions.MAIN_MARK_OPTION_D)
        }
    }

    fun sendCurrent(position: Int) {
        sendAction(Actions.CONFIG_SELECT_QUEST + "|" + position)
    }

    fun sendLifelineAction(option: LifelinesEnum) {
        when (option) {
            LifelinesEnum.ShowPeopleForm -> {
                sendAction(Actions.LIFE_SHOW_PPL_FORM)
            }
            LifelinesEnum.Show50 -> {
                sendAction(Actions.LIFE_SHOW_50)
            }
            LifelinesEnum.TogglePhone -> {
                sendAction(Actions.LIFE_TOGGLE_PHONE)
            }
            LifelinesEnum.ToggleChart -> {
                sendAction(Actions.LIFE_TOGGLE_CHART)
            }
            LifelinesEnum.ToggleGroup -> {
                sendAction(Actions.LIFE_TOGGLE_GROUP)
            }
            LifelinesEnum.Toggle50 -> {
                sendAction(Actions.LIFE_TOGGLE_50)
            }
            LifelinesEnum.ShowNextRewardOnTable -> {
                sendAction(Actions.LIFE_TABLE_SHOW_REWARD)
            }
        }
    }

    fun sendSettingsAction(option: SettingsEnum) {
        when (option) {
            SettingsEnum.ShowOpening -> {
                sendAction(Actions.CONFIG_SHOW_OPENING)
            }
            SettingsEnum.SelectCurrent -> {
                sendAction(Actions.CONFIG_SELECT_QUEST)
            }
            SettingsEnum.ShowCurrentQuestion -> {
                sendAction(Actions.CONFIG_NAV_QUEST)
            }
            SettingsEnum.ResetUI -> sendAction(Actions.CONFIG_RESET_UI)
        }
    }

    private fun sendAction(action: String) {
        mChatService!!.write(action.toByteArray())
    }
}