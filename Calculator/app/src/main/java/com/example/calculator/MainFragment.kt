package com.example.calculator

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.calculator.databinding.FragmentMainBinding
import com.example.calculator.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }
    private var _binding:FragmentMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //lien ket viewModel
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.textCaculator.observe(requireActivity()){
            Log.d(TAG, "onViewCreated: ${it}")
        }
    }

    companion object {
        const val TAG = "1234"
    }
}