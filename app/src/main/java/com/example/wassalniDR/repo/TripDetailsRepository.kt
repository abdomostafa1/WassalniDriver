package com.example.wassalniDR.repo

import com.example.wassalniDR.data.TripDetails
import com.example.wassalniDR.datasource.TripDetailsDataSource
import javax.inject.Inject

class TripDetailsRepository @Inject constructor(private val tripDetailsDataSource: TripDetailsDataSource)
{
    suspend fun getTripDetails(id:String):TripDetails
    {
        return tripDetailsDataSource.getTripDetails(id)
    }
}