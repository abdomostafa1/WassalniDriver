package com.example.wassalniDR.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.wassalniDR.R
import com.example.wassalniDR.database.TripsRetrofit
import com.example.wassalniDR.databinding.FragmentTripDetailsBinding
import com.example.wassalniDR.databinding.FragmentTripsBinding
import com.example.wassalniDR.datasource.TripDetailsDataSource
import com.example.wassalniDR.datasource.TripsDataSource
import com.example.wassalniDR.repo.TripDetailsRepositry
import com.example.wassalniDR.repo.TripRepositry
import com.example.wassalniDR.util.Constant
import com.example.wassalniDR.viewModels.TripDetailsViewModel
import com.example.wassalniDR.viewModels.TripsViewModel
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class TripDetailsFragment : Fragment() {
    private lateinit var tripsDetailsDataSource: TripDetailsDataSource
    private lateinit var binding: FragmentTripDetailsBinding
    private lateinit var tripsDetailsViewModel: TripDetailsViewModel
    private lateinit var repo: TripDetailsRepositry
    private lateinit var retrofit: TripsRetrofit




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentTripDetailsBinding.inflate(inflater)
        retrofit=
            Retrofit.Builder().baseUrl(Constant.BASEURL).addConverterFactory(MoshiConverterFactory.create())
                .build().create(TripsRetrofit::class.java)
        tripsDetailsDataSource= TripDetailsDataSource(requireActivity().applicationContext,retrofit)
        repo= TripDetailsRepositry(tripsDetailsDataSource)
        tripsDetailsViewModel= TripDetailsViewModel(repo)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }


}