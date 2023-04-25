package com.example.wassalniDR.datasource

import android.content.Context
import android.util.Log
import com.example.wassalniDR.data.Trips
import com.example.wassalniDR.database.TripsRetrofit

class TripsDataSource(val context:Context,val tripService: TripsRetrofit)
{
    private val TAG = "TripsDataSource"

    fun getTrips(token: String): List<Trips> {
        val task = tripService.getAllTrips(token).execute()
        if (task.isSuccessful) {
            Log.e(TAG, "isSuccessful ")
            val allTripsResponse = task.body()
            val trips = allTripsResponse?.Trips
            Log.e(TAG, "Trips: ${trips.toString()}")
            return trips!!
        } else {
            val error = task.errorBody()?.string()
            throw Exception(error)
        }

    }




}
data class AllTripsResponse(val Trips: List<Trips>)

