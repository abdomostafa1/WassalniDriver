package com.example.wassalniDR.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.PreferenceManager
import com.example.wassalniDR.adapters.TripsAdapter
import com.example.wassalniDR.data.uiState.TripUiState
import com.example.wassalniDR.database.TripsRetrofit
import com.example.wassalniDR.databinding.FragmentTripsBinding
import com.example.wassalniDR.datasource.TripsDataSource
import com.example.wassalniDR.repo.TripRepositry
import com.example.wassalniDR.util.Constant
import com.example.wassalniDR.viewModels.TripsViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val TAG = "TripsFragment"
class TripsFragment : Fragment() {

    private lateinit var tripsDataSource: TripsDataSource
    private lateinit var binding: FragmentTripsBinding
    private lateinit var tripsViewModel: TripsViewModel
    private lateinit var repo: TripRepositry
    private lateinit var adapter: TripsAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTripsBinding.inflate(inflater)
        val retrofit = Retrofit.Builder().baseUrl(Constant.BASEURL)
            .addConverterFactory(
                MoshiConverterFactory.create(Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build())).build()
        val tripRetrofit=retrofit.create(TripsRetrofit::class.java)
        tripsDataSource = TripsDataSource(tripRetrofit)
        repo = TripRepositry(tripsDataSource)
        tripsViewModel = TripsViewModel(repo)
        adapter = TripsAdapter()
        binding.rvTrips.adapter = adapter
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = sharedPreferences.getString("token", "")
        Log.e("TAG", "token equals:$token")
        tripsViewModel.getTrips(token!!)
        handleTripsLiveData()

        binding.errorState.retry.setOnClickListener {
            tripsViewModel.getTrips(token)
        }
    }

    private fun handleTripsLiveData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripsViewModel.state.collect {
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