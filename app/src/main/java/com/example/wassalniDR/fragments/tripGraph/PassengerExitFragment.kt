package com.example.wassalniDR.fragments.tripGraph

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.example.wassalniDR.R
import com.example.wassalniDR.adapters.ItemDecorator
import com.example.wassalniDR.adapters.PassengerExitAdapter
import com.example.wassalniDR.databinding.FragmentExitingPassengersBinding
import com.example.wassalniDR.viewModels.TripDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PassengerExitFragment : Fragment() {

    lateinit var binding: FragmentExitingPassengersBinding
    private val viewModel : TripDetailsViewModel by navGraphViewModels(R.id.trip_graph){
        defaultViewModelProviderFactory
    }
    private lateinit var onClickLeaveBtn :(String)->Unit
    @Inject
    lateinit var adapter: PassengerExitAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentExitingPassengersBinding.inflate(layoutInflater)
        onClickLeaveBtn = viewModel::recordPassengerArrival

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.topAppBar.setNavigationOnClickListener {
            it.findNavController().navigateUp()
        }
        val passengersOfPastStations=viewModel.getPassengersOfPastStations()
        if (passengersOfPastStations.isEmpty())
            binding.noPassengers.visibility = View.VISIBLE
        else {
            binding.recyclerView.adapter = adapter
            val pixelsSize = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._8sdp)
            binding.recyclerView.addItemDecoration(ItemDecorator(pixelsSize))

            adapter.setData(passengersOfPastStations, viewModel.getTripPrice())
            adapter.setOnClickListeners(onClickLeaveBtn)
        }

    }
}