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
import com.example.wassalniDR.adapters.PassengerEntryAdapter
import com.example.wassalniDR.data.passengers
import com.example.wassalniDR.databinding.FragmentEnteringPassengersBinding
import com.example.wassalniDR.viewModels.TripDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PassengerEntryFragment : Fragment() {

    lateinit var binding:FragmentEnteringPassengersBinding
    private val viewModel :TripDetailsViewModel by navGraphViewModels(R.id.trip_graph){
        defaultViewModelProviderFactory
    }
    private lateinit var onClickAttendBtn :(String)->Unit
    private lateinit var onClickNotAttendBtn :(String)->Unit
    @Inject
    lateinit var adapter:PassengerEntryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentEnteringPassengersBinding.inflate(layoutInflater)

        onClickAttendBtn = viewModel::recordPassengerAttendance
        onClickNotAttendBtn= viewModel::recordPassengerAbsence

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topAppBar.setNavigationOnClickListener {
            it.findNavController().navigateUp()
        }
        val passengersOfCurrentStation=viewModel.getPassengersOfCurrentStation()
        if (passengersOfCurrentStation.isEmpty())
            binding.noPassengers.visibility=View.VISIBLE
        else {
            binding.recyclerView.adapter = adapter
            val pixelsSize = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._8sdp)
            binding.recyclerView.addItemDecoration(ItemDecorator(pixelsSize))

            adapter.setData(passengersOfCurrentStation)
            adapter.setOnClickListeners(onClickAttendBtn, onClickNotAttendBtn)

        }
    }

}