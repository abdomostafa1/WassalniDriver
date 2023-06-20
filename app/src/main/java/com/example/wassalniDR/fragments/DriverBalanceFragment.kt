package com.example.wassalniDR.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.wassalniDR.R
import com.example.wassalniDR.data.uiState.DriverBalanceUiState
import com.example.wassalniDR.data.uiState.TripUiState
import com.example.wassalniDR.databinding.FragmentDriverBalanceBinding
import com.example.wassalniDR.viewModels.BalanceViewModel
import kotlinx.coroutines.launch


class DriverBalanceFragment : Fragment() {

    lateinit var binding:FragmentDriverBalanceBinding
    private val viewModel:BalanceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentDriverBalanceBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.balanceUiState.collect { state ->
                    when (state) {
                        is DriverBalanceUiState.Loading -> {
                            binding.loader.visibility=View.VISIBLE
                        }
                        is DriverBalanceUiState.Success -> {
                            binding.loader.visibility=View.GONE
                            binding.balance.text=state.balance.toString()
                        }
                        is DriverBalanceUiState.Error -> {
                            binding.loader.visibility=View.GONE
                            Toast.makeText(requireActivity(),state.errorMsg,Toast.LENGTH_LONG).show()
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
}