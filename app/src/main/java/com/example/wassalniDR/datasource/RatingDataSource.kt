package com.example.wassalniDR.datasource

import android.media.Rating
import android.util.Log
import com.example.wassalniDR.database.TripsRetrofit
import org.json.JSONObject

private const val TAG = "RatingDataSource"
class RatingDataSource(private val tripService: TripsRetrofit) {
    fun getRating(token:String):List<com.example.wassalniDR.data.Rating>
    {
        val task=tripService.getRating(token).execute()
        if(task.isSuccessful)
        {
            Log.e(TAG, "isSuccessful")
            val rating=task.body()?.Ratings
            Log.e(TAG, "Trips: ${rating.toString()}")
            return rating!!
        }else {
            var errorMessage=""
            val error = task.errorBody()?.string()
            val json=JSONObject(error)
            errorMessage=json.getString("message")
            throw Exception(errorMessage)
        }

    }
}


data class driverRatingResponse(val Ratings:List<com.example.wassalniDR.data.Rating>)