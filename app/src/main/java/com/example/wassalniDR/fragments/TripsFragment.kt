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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class TripsFragment:Fragment() {

    private lateinit var tripsDataSource: TripsDataSource
    private lateinit var binding:FragmentTripsBinding
    private lateinit var tripsViewModel: TripsViewModel
    private lateinit var repo:TripRepositry
    private lateinit var retrofit: TripsRetrofit
    private lateinit var adapter: TripsAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentTripsBinding.inflate(inflater)
        retrofit=Retrofit.Builder().baseUrl(Constant.BASEURL).addConverterFactory(MoshiConverterFactory.create())
            .build().create(TripsRetrofit::class.java)
        tripsDataSource= TripsDataSource(requireActivity().applicationContext,retrofit)
        repo= TripRepositry(tripsDataSource)
        tripsViewModel= TripsViewModel(repo)
        adapter = TripsAdapter()
        binding.rvTrips.adapter = adapter
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(requireContext())
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token= sharedPreferences.getString("token", "")
        Log.e("token",token!!)
        if (token != null) {
            tripsViewModel.getTrips(token)
            handleTripsLiveData()
        }

    }

    private fun handleTripsLiveData() {
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripsViewModel.state.collect{ state->
                    when(state)
                    {
                        is TripUiState.Loading->{

                        }
                        is TripUiState.Success->{
                            val trips=state.trips
                            adapter.setData(trips)
                        }
                        is TripUiState.Error->{
                            Toast.makeText(requireContext(), "error", Toast.LENGTH_LONG).show()
                        }
                        is TripUiState.Empty->{

                        }
                        else ->{

                        }
                    }

                }
            }
        }
    }


}