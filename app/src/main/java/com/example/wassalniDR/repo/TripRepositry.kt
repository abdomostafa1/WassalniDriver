package com.example.wassalniDR.repo

import com.example.wassalniDR.data.Driver
import com.example.wassalniDR.data.Trip
import com.example.wassalniDR.datasource.TripsDataSource

class TripRepositry(private val tripsDataSource: TripsDataSource)
{
    suspend fun getTrips(token: String):List<Trip>
    {
        return tripsDataSource.getTrips(token)
    }

    fun retrieveDriverData() : Driver {
        return tripsDataSource.retrieveDriverData()
    }
}