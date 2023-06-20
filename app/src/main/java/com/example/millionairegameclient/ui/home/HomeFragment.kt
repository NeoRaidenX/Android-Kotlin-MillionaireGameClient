package com.example.millionairegameclient.ui.home

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
import com.example.millionairegameclient.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this)[HomeViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val recyclerview = binding.recyclerview
        recyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
        val mainAdapter = MainAdapter(requireContext()) { optionSelected ->

            when(optionSelected) {
                MainOptionsEnum.ShowOption -> showOptionAlertDialog(false)
                MainOptionsEnum.MarkOption -> showOptionAlertDialog(true)
                else -> {
                    lifecycleScope.launch {
                        viewModel.sendAction(optionSelected)
                    }
                }
            }

        }
        recyclerview.adapter = mainAdapter
        return binding.root
    }

    private fun showOptionAlertDialog(isMarkOption: Boolean) {
        AlertDialog.Builder(requireContext())
            .setItems(R.array.options) { dialogInterface: DialogInterface, position: Int ->
                if (isMarkOption) viewModel.sendMarkOption(position) else viewModel.sendShowOption(position)
            }
            .show()
    }

}