package com.example.wassalniDR.database

import android.telecom.Call
import com.example.wassalniDR.data.TripDetails
import com.example.wassalniDR.data.Trips
import com.example.wassalniDR.datasource.AllTripsResponse


import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

@JvmSuppressWildcards
interface TripsRetrofit {
    @GET("getAllTripsForDriver")
   fun getAllTrips(@Header("token") token:String):retrofit2.Call<AllTripsResponse>

   @GET("")
   fun getTripDetails(@Path("id") id: String):retrofit2.Call<TripDetails>
}