package com.example.wassalniDR.datasource

import android.content.Context
import android.util.Log
import com.example.wassalniDR.data.Trip
import com.example.wassalniDR.database.TripsRetrofit
import com.squareup.moshi.JsonClass

private const val TAG = "TripsDataSource"

class TripsDataSource(private val tripService: TripsRetrofit) {

    fun getTrips(token: String): List<Trip> {
        val task = tripService.getAllTrips(token).execute()
        if (task.isSuccessful) {
            Log.e(TAG, "isSuccessful ")
            val trips = task.body()?.Trips
            Log.e(TAG, "Trips: ${trips.toString()}")
            return trips!!
        } else {
            val error = task.errorBody()?.string()
            throw Exception(error)
        }

    }

}

data class DriverTripsResponse(val Trips: List<Trip>)

