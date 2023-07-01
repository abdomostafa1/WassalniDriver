package com.example.wassalniDR.datasource

import android.util.Log
import com.example.wassalniDR.data.Trip


import com.example.wassalniDR.database.TripsRetrofit
private const val TAG = "FinishedTripsDataSource"
class FinishedTripsDataSource(private val tripService: TripsRetrofit)
{
    fun getPerviousTrips(token: String): List<Trip> {
        val task = tripService.getPerviousTrips(token).execute()
        if (task.isSuccessful) {
            Log.e(TAG, "isSuccessful ")
            val trips = task.body()?.trips
            Log.e(TAG, "Trips: ${trips.toString()}")
            return trips!!
        } else {
            val error = task.errorBody()?.string()
            throw Exception(error)
        }

    }
}
data class DriverFinishedTripsResponse(val trips: List<Trip>)