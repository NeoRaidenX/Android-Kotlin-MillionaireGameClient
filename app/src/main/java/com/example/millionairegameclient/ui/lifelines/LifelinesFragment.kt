package com.example.millionairegameclient.ui.lifelines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.millionairegameclient.databinding.FragmentLifelinesBinding
import kotlinx.coroutines.launch

class LifelinesFragment : Fragment() {

    private lateinit var binding: FragmentLifelinesBinding
    private lateinit var viewModel: LifelinesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this)[LifelinesViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLifelinesBinding.inflate(inflater, container, false)

        val recyclerview = binding.recyclerview
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        val mainAdapter = LifelineAdapter { optionSelected ->
            lifecycleScope.launch {
                viewModel.sendAction(optionSelected)
            }
        }
        recyclerview.adapter = mainAdapter
        return binding.root
    }
}