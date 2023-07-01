package com.example.wassalniDR.repo


import com.example.wassalniDR.data.Trip
import com.example.wassalniDR.datasource.SupportDataSource
import com.example.wassalniDR.datasource.TripsDataSource

class SupportRepository(private val supportDataSource: SupportDataSource) {
    suspend fun getTrips(token: String):List<Trip>
    {
        return supportDataSource.getTrips(token)
    }
}