package com.example.millionairegameclient.ui.lifelines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.millionairegameclient.CommunicationRepository
import com.example.millionairegameclient.databinding.FragmentLifelinesBinding
import com.example.millionairegameclient.ui.home.MainAdapter
import kotlinx.coroutines.launch

class LifelinesFragment : Fragment() {

    private var _binding: FragmentLifelinesBinding? = null
    private val repository = CommunicationRepository()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val lifelinesViewModel =
            ViewModelProvider(this).get(LifelinesViewModel::class.java)

        _binding = FragmentLifelinesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerview = binding.recyclerview
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        val mainAdapter = LifelineAdapter { optionSelected ->
            lifecycleScope.launch {
                repository.sendLifelineAction(requireContext(), root, optionSelected)
            }
        }
        recyclerview.adapter = mainAdapter
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}