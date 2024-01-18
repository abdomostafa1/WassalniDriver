package com.example.wassalniDR.repo


import com.example.wassalniDR.data.Trip
import com.example.wassalniDR.datasource.FinishedTripsDataSource

class FinishedTripRepository(private val finishedTripsDataSource: FinishedTripsDataSource)
{
    suspend fun getPreviousTrips(token: String):List<Trip>
    {
        return finishedTripsDataSource.getPreviousTrips(token)
    }
}