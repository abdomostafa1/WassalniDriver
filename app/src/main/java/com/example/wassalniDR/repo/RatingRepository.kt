package com.example.wassalniDR.repo

import android.media.Rating
import com.example.wassalniDR.datasource.RatingDataSource
import com.example.wassalniDR.datasource.TripsDataSource

class RatingRepository(private val ratingDataSource: RatingDataSource) {
    suspend fun getRating(token:String):List<com.example.wassalniDR.data.Rating>
    {
        return ratingDataSource.getRating(token)
    }
}