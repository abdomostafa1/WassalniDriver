package com.example.wassalniDR.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.wassalniDR.data.Driver
import com.example.wassalniDR.databinding.FragmentBalanceBinding
import com.example.wassalniDR.viewModels.BalanceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "BalanceFragment"
@AndroidEntryPoint
class BalanceFragment : Fragment() {

    lateinit var binding: FragmentBalanceBinding
    private val viewModel: BalanceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBalanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val driver=viewModel.retrieveDriverData()
                Log.e(TAG, "driver:$driver " )
                updateUi(driver)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.loader.visibility=View.GONE
                    if (e.message!=null)
                        Toast.makeText(requireActivity(),e.message,Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private suspend fun updateUi(driver:Driver) {
        withContext(Dispatchers.Main){
            binding.loader.visibility=View.GONE
            binding.balance.text=driver.balance.toString()
        }
    }
}