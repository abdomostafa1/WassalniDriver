package com.example.wassalniDR.datasource

import android.content.Context
import android.util.Log
import com.example.wassalniDR.data.TripDetails
import com.example.wassalniDR.database.TripsRetrofit

class TripDetailsDataSource(val context: Context,val tripService: TripsRetrofit)
{
    private val TAG = "TripDetailsDataSource"

    fun getTripDetails(id:String):TripDetails
    {
        val task= tripService.getTripDetails(id).execute()
        if(task.isSuccessful)
        {
            val tripDetails=task.body()!!
            Log.e(TAG, "getTripDetails: $tripDetails")
            return tripDetails
        }else
        {
            throw Exception(task.errorBody()?.string())
        }
    }

}