package com.example.millionairegameclient.ui.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.millionairegameclient.databinding.FragmentBluetoothBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BluetoothFragment : Fragment() {

    companion object {
        private const val TAG = "BluetoothFragment"
    }

    private lateinit var binding: FragmentBluetoothBinding
    private lateinit var viewModel: BluetoothViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this)[BluetoothViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBluetoothBinding.inflate(inflater, container, false)



        val bluetoothManager: BluetoothManager = requireContext().getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        val pairedDevices: MutableSet<android.bluetooth.BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        val adapter = BtDeviceAdapter {device -> adapterOnClick(device)}
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = adapter
        pairedDevices?.toList()?.let { adapter.updateDevices(it) }

        binding.test.setOnClickListener {
            //viewModel.sendMessage("test".toByteArray())
            viewModel.test()
        }

        /*val adapter = BtDeviceAdapter {device -> adapterOnClick(device)}
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.test.setOnClickListener {
            viewModel.sendMessage("test".toByteArray())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect {
                Log.d(TAG, "state collect: $it")
                when {
                    it.isConnecting -> {
                        Log.d(TAG, "isConnecting: ")
                    }
                    it.isConnected -> {
                        Log.d(TAG, "isConnected: ")
                    }
                    else -> {
                        Log.d(TAG, "else: ")
                        adapter.updateDevices(it.pairedDevices + it.scannedDevices)
                    }
                }
            }
        }*/

        /*if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val bluetoothManager: BluetoothManager = requireContext().getSystemService(BluetoothManager::class.java)
            val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
            val adapter = BtDeviceAdapter {device -> adapterOnClick(device)}
            binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerview.adapter = adapter
            pairedDevices?.toList()?.let { adapter.updateDevices(it) }
        }*/

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun adapterOnClick(device: BluetoothDevice) {
        Log.d(TAG, "adapterOnClick: ")
        viewModel.connectToDevice(device)
        /*lifecycleScope.launch {
            viewModel.connectToDevice(device)
        }*/
    }
}