package com.example.millionairegameclient.ui.settings

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.millionairegameclient.R
import com.example.millionairegameclient.databinding.FragmentSettingsBinding
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this)[SettingsViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        val recyclerview = binding.recyclerview
        recyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
        val mainAdapter = SettingsAdapter { optionSelected ->
            lifecycleScope.launch {

                when (optionSelected) {
                    SettingsEnum.SelectCurrent -> {
                        showOptionAlertDialog()
                    }
                    else -> {
                        viewModel.sendAction(optionSelected)
                    }
                }

            }
        }
        recyclerview.adapter = mainAdapter
        return binding.root
    }

    private fun showOptionAlertDialog() {
        AlertDialog.Builder(requireContext())
            .setItems(R.array.current) { dialogInterface: DialogInterface, position: Int ->
                viewModel.sendCurrent(position)
            }
            .show()
    }
}