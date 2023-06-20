package com.example.wassalniDR.fragments.tripGraph

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.wassalniDR.R
import com.example.wassalniDR.data.uiState.ConfirmArrivalUiState
import com.example.wassalniDR.databinding.FragmentRideLifecycleBinding
import com.example.wassalniDR.util.DateUseCase
import com.example.wassalniDR.util.Permission
import com.example.wassalniDR.viewModels.TripDetailsViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RideLifecycleFragment : Fragment() {

    lateinit var binding: FragmentRideLifecycleBinding
    private val viewModel: TripDetailsViewModel by navGraphViewModels(R.id.trip_graph) {
        defaultViewModelProviderFactory
    }
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var permission: Permission
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRideLifecycleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        viewModel.updateTripLifecycleState()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.onNewStation.collect { station ->
                    if (station != null) {
                        binding.navigateTo.text = station.name
                        binding.confirmTv.text = station.name
                        binding.arrivalTime.text = DateUseCase.fromMillisToHhMma(station.time)
                    }
                }
            }
        }

        viewModel.confirmArrivalState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ConfirmArrivalUiState.Loading -> {
                    binding.loader.visibility = View.VISIBLE
                }
                is ConfirmArrivalUiState.Success -> {
                    binding.loader.visibility = View.INVISIBLE
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.arrival_confirmed),
                        Toast.LENGTH_LONG
                    ).show()
                    hideConfirmArrivalBtnTemporarily()
                }
                is ConfirmArrivalUiState.Error -> {
                    binding.loader.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), state.errorMsg, Toast.LENGTH_LONG).show()
                }
                else -> {}
            }

        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tripFinishState.collect {
                    if (it)
                        showTripFinishState()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.lastStationState.collect {
                    if (it)
                        showLastStationState()
                }
            }
        }

        setOnClickListeners()
    }

    private fun hideConfirmArrivalBtnTemporarily() {
        lifecycleScope.launch {
            binding.confirmCard.visibility = View.INVISIBLE
            delay(10000)
            binding.confirmCard.visibility = View.VISIBLE
        }
    }

    private fun setOnClickListeners() {

        binding.navigationCard.setOnClickListener {
            Log.e("TAG", "setOnClickListeners: navigationCard")
            val location = viewModel.getNextStationLocation()
            openGoogleMapNavigation(location)
        }
        binding.confirmCard.setOnClickListener {
            confirmArrival()
        }
        binding.enterPassengersBtn.setOnClickListener {
            findNavController().navigate(R.id.action_rideLifecycleFragment_to_passengerEntryFragment)
        }
        binding.exitingPassengersBtn.setOnClickListener {
            findNavController().navigate(R.id.action_rideLifecycleFragment_to_passengerExitFragment)
        }
        binding.tripFinishView.okButton.setOnClickListener {
            findNavController().navigate(R.id.action_rideLifecycleFragment_to_tripFareFragment)
        }
    }

    private fun confirmArrival() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permission.requestLocationPermission(::confirmArrival)
            return
        }
        if (!permission.isGpsOpen()) {
            permission.openGps(::confirmArrival, false)
            return
        }
        binding.loader.visibility = View.VISIBLE
        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                val driverLocation = LatLng(location.latitude, location.longitude)
                viewModel.confirmArrival(driverLocation)
            }
    }

    private fun openGoogleMapNavigation(location: LatLng) {
        // Create a Uri from an intent string. Use the result to create an Intent.
        val gmmIntentUri =
            Uri.parse("google.navigation:q=${location.latitude},${location.longitude}")

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps")

        mapIntent.resolveActivity(requireActivity().packageManager)?.let {
            startActivity(mapIntent)
        }
        // Attempt to start an activity that can handle the Intent

    }

    private fun showTripFinishState() {
        binding.tripFinishView.root.visibility = View.VISIBLE
        binding.lastStationView.root.visibility = View.GONE
    }

    private fun showLastStationState() {
        binding.lastStationView.root.visibility = View.VISIBLE
        binding.tripFinishView.root.visibility = View.GONE
    }


}