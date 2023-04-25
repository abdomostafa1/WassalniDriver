package com.example.wassalniDR.repo

import com.example.wassalniDR.data.Trips
import com.example.wassalniDR.datasource.TripsDataSource

class TripRepositry(private val tripsDataSource: TripsDataSource)
{
    suspend fun getTrips(token: String):List<Trips>
    {
//        val params=HashMap<String,Any>()
//        params["token"]=token
        return tripsDataSource.getTrips(token)
    }
}