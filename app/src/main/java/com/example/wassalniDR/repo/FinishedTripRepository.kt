package com.example.wassalniDR.repo


import com.example.wassalniDR.data.Trip
import com.example.wassalniDR.datasource.FinishedTripsDataSource

import com.example.wassalniDR.datasource.TripsDataSource

class FinishedTripRepository(private val finishedTripsDataSource: FinishedTripsDataSource)
{
    suspend fun getPerviousTrips(token: String):List<Trip>
    {
        return finishedTripsDataSource.getPerviousTrips(token)
    }
}