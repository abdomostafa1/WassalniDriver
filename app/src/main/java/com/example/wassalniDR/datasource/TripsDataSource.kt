package com.example.wassalniDR.datasource

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.wassalniDR.data.Driver
import com.example.wassalniDR.data.LoggedInDriver
import com.example.wassalniDR.data.Trip

import com.example.wassalniDR.database.TripsRetrofit
import com.squareup.moshi.JsonClass
import org.json.JSONObject

private const val TAG = "TripsDataSource"

class TripsDataSource(private val tripService: TripsRetrofit, private val sharedPreferences: SharedPreferences) {

    fun getTrips(token: String): List<Trip> {
        val task = tripService.getUpComingTrips(token).execute()
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

    suspend fun retrieveDriverData(): Driver {
        val token = sharedPreferences.getString("token","")
        val request = tripService.retrieveDriverData(token!!).execute()
        if (request.isSuccessful)
            return request. body()!!
        else {
            var errorMsg = ""
            request.errorBody()?.let { errorMsg = handleErrorMessage(it.string()) }
            throw Exception(errorMsg)
        }

    }

    private fun handleErrorMessage(json: String): String {
        val root = JSONObject(json)
        return root.getString("msg")
    }

}

data class DriverTripsResponse(val trips: List<Trip>)

