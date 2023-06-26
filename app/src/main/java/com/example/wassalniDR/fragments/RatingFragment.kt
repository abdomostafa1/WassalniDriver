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
import androidx.preference.PreferenceManager
import com.example.wassalniDR.R
import com.example.wassalniDR.adapters.RatingAdapter
import com.example.wassalniDR.adapters.TripsAdapter
import com.example.wassalniDR.data.uiState.RatingUiState
import com.example.wassalniDR.data.uiState.TripUiState
import com.example.wassalniDR.database.TripsRetrofit
import com.example.wassalniDR.databinding.FragmentRatingBinding
import com.example.wassalniDR.databinding.FragmentTripsBinding
import com.example.wassalniDR.datasource.RatingDataSource
import com.example.wassalniDR.datasource.TripsDataSource
import com.example.wassalniDR.repo.RatingRepository
import com.example.wassalniDR.repo.TripRepositry
import com.example.wassalniDR.util.Constant
import com.example.wassalniDR.viewModels.RatingViewModel
import com.example.wassalniDR.viewModels.TripsViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class RatingFragment : Fragment() {

    private lateinit var ratingDataSource: RatingDataSource
    private lateinit var binding: FragmentRatingBinding
    private lateinit var ratingViewModel: RatingViewModel
    private lateinit var repo: RatingRepository
    private lateinit var adapter: RatingAdapter
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRatingBinding.inflate(inflater)
        val retrofit = Retrofit.Builder().baseUrl(Constant.BASEURL1)
            .addConverterFactory(
                MoshiConverterFactory.create(Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build())).build()
        val ratingRetrofit=retrofit.create(TripsRetrofit::class.java)
        ratingDataSource = RatingDataSource(ratingRetrofit)
        repo = RatingRepository(ratingDataSource)
        ratingViewModel = RatingViewModel(repo)
        adapter = RatingAdapter()
        binding.rvTrips.adapter = adapter
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = sharedPreferences.getString("token", "")
        Log.e("TAG", "token equals:$token")
        ratingViewModel.getRating(token!!)
        handleLiveDataRating()
    }

    private fun handleLiveDataRating() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                ratingViewModel.state.collect {
                    when (it) {
                        is RatingUiState.Loading -> {
                            showLoadingState()
                        }
                        is RatingUiState.Success -> {
                            showSuccessState()
                            adapter.setData(it.rating)
                        }
                        is RatingUiState.Error -> {
                            showErrorState()
                            Toast.makeText(requireContext(), it.errorMsg, Toast.LENGTH_LONG).show()
                        }
                        is RatingUiState.Empty -> {
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