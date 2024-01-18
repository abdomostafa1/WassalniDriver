package com.example.wassalniDR.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.wassalniDR.adapters.FinishedTripsAdapter
import com.example.wassalniDR.data.uiState.TripUiState
import com.example.wassalniDR.database.TripsRetrofit
import com.example.wassalniDR.databinding.FragmentPreviousTripsBinding
import com.example.wassalniDR.datasource.FinishedTripsDataSource
import com.example.wassalniDR.repo.FinishedTripRepository
import com.example.wassalniDR.util.Constant
import com.example.wassalniDR.viewModels.FinishedTripsViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val TAG = "PreviousTripsFragment"

class PreviousTripsFragment : Fragment() {

    private lateinit var finishedTripsDataSource: FinishedTripsDataSource
    private lateinit var binding: FragmentPreviousTripsBinding
    private lateinit var finishedTripViewModel: FinishedTripsViewModel
    private lateinit var repo: FinishedTripRepository
    private lateinit var adapter: FinishedTripsAdapter
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.e(TAG, "onCreateView:FinishedTripsFragment " )

        binding = FragmentPreviousTripsBinding.inflate(inflater)
        val retrofit = Retrofit.Builder().baseUrl(Constant.BASEURL)
            .addConverterFactory(
                MoshiConverterFactory.create(Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build())).build()

        val finishedTripRetrofit=retrofit.create(TripsRetrofit::class.java)
        finishedTripsDataSource = FinishedTripsDataSource(finishedTripRetrofit)
        repo = FinishedTripRepository(finishedTripsDataSource)
        finishedTripViewModel = FinishedTripsViewModel(repo)
        adapter = FinishedTripsAdapter()
        binding.rvTrips.adapter = adapter
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = sharedPreferences.getString("token", "")
        Log.e("TAG", "token equals:$token")
        handleLiveDataForFinishedTrips()
        finishedTripViewModel.getPreviousTrips(token!!)
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun handleLiveDataForFinishedTrips() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                finishedTripViewModel.state.collect {
                    when (it) {
                        is TripUiState.Loading -> {
                            showLoadingState()
                        }
                        is TripUiState.Success -> {
                            showSuccessState()
                            adapter.setData(it.trips)
                        }
                        is TripUiState.Error -> {
                            showErrorState()
                            Toast.makeText(requireContext(), it.errorMsg, Toast.LENGTH_LONG).show()
                        }
                        is TripUiState.Empty -> {
                            showEmptyState()
                        }
                    }
                }
            }
        }
    }

    private fun showLoadingState() {
        binding.errorState.root.visibility = View.GONE
        binding.emptyState.root.visibility = View.GONE
        binding.loadingState.root.visibility = View.VISIBLE

    }

    private fun showSuccessState() {
        binding.loadingState.root.visibility = View.GONE
        binding.errorState.root.visibility = View.GONE
        binding.emptyState.root.visibility = View.GONE
        binding.rvTrips.visibility = View.VISIBLE

    }

    private fun showErrorState() {
        binding.loadingState.root.visibility = View.GONE
        binding.emptyState.root.visibility = View.GONE
        binding.rvTrips.visibility = View.GONE
        binding.errorState.root.visibility = View.VISIBLE
    }

    private fun showEmptyState() {
        binding.loadingState.root.visibility = View.GONE
        binding.errorState.root.visibility = View.GONE
        binding.rvTrips.visibility = View.GONE
        binding.emptyState.root.visibility = View.VISIBLE
    }


}