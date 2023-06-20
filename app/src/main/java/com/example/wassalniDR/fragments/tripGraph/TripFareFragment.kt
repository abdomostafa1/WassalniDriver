package com.example.wassalniDR.fragments.tripGraph

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.wassalniDR.R
import com.example.wassalniDR.data.uiState.TripFareUiState
import com.example.wassalniDR.data.uiState.TripUiState
import com.example.wassalniDR.databinding.FragmentTripFareBinding
import com.example.wassalniDR.databinding.FragmentTripFareListBinding
import com.example.wassalniDR.fragments.tripGraph.placeholder.PlaceholderContent
import com.example.wassalniDR.viewModels.TripDetailsViewModel
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items.
 */
class TripFareFragment : Fragment() {

    lateinit var binding:FragmentTripFareListBinding
    private val viewModel:TripDetailsViewModel by navGraphViewModels(R.navigation.trip_graph)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentTripFareListBinding.inflate(layoutInflater, container, false)
        binding.list.apply {
            // Set the adapter
            val passengers=viewModel.getAttendPassengers()
            val tripPrice=viewModel.getTripPrice()
            val myAdapter=TripFareAdapter(passengers,tripPrice)
            adapter=myAdapter

        }

        binding.total.text="${viewModel.calculateTotal()}"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.finishButton.setOnClickListener {
            viewModel.finishTrip()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tripFareUiState.collect { state ->
                    when (state) {
                        is TripFareUiState.Loading -> {
                            binding.loader.visibility=View.VISIBLE
                        }
                        is TripFareUiState.Success -> {
                            binding.loader.visibility=View.INVISIBLE
                            findNavController().navigate(R.id.action_to_main_graph)
                        }
                        is TripFareUiState.Error -> {
                            binding.loader.visibility=View.INVISIBLE
                        }
                        else -> Unit
                    }
                }
            }
        }

    }
}