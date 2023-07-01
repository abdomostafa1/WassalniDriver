package com.example.wassalniDR.repo

import android.util.Log
import com.example.wassalniDR.data.Station
import com.example.wassalniDR.data.TripDetails
import com.example.wassalniDR.database.StationArriveResponse
import com.example.wassalniDR.datasource.TripDetailsDataSource
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import javax.inject.Inject
import kotlin.math.*

private const val TAG = "TripDetailsRepository"
class TripDetailsRepository @Inject constructor(private val tripDetailsDataSource: TripDetailsDataSource) {
    suspend fun getTripDetails(id: String): TripDetails {
        return tripDetailsDataSource.getTripDetails(id)
    }

    fun calculateDistance(location1: LatLng, location2: LatLng) :Double{
            return haversine(location1.latitude,location1.longitude,location2.latitude,location2.longitude)
    }

    private fun haversine(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        // distance between latitudes and longitudes
        var lat1 = lat1
        var lat2 = lat2
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        // convert to radians
        lat1 = Math.toRadians(lat1)
        lat2 = Math.toRadians(lat2)

        // apply formulae
        val a = sin(dLat / 2).pow(2.0) +
                sin(dLon / 2).pow(2.0) *
                cos(lat1) *
                cos(lat2)
        val rad = 6371.0
        val c = 2 * asin(sqrt(a))
        return rad * c * 1000
    }

    suspend fun confirmArrival(tripId: String,stationIndex: Int) :StationArriveResponse{
        return tripDetailsDataSource.confirmArrival(tripId,stationIndex)

    }

    fun recordPassengerAttendance(tripId:String, passengerTicket:Int) {
        tripDetailsDataSource.recordPassengerAttendance(tripId,passengerTicket)
    }
    fun recordPassengerAbsence(tripId:String, passengerTicket:Int) {
        tripDetailsDataSource.recordPassengerAbsence(tripId, passengerTicket)
    }

    fun recordPassengerArrival(tripId:String,passengerTicket:Int) {
        tripDetailsDataSource.recordPassengerArrival(tripId, passengerTicket)
    }

    fun getPolyLine(stations: List<Station>): List<LatLng> {
        val first = stations[0].location
        val last = stations[stations.size - 1].location
        val origin = "${first.lat},${first.lng}"
        val destination = "${last.lat},${last.lng}"

        val wayPoints = decodeWapPoints(stations)
        Log.e(TAG, "origin:$origin & destination:$destination & waypoint:$wayPoints")
        Log.e(TAG, "origin:$origin::dest:$destination  ")
        return tripDetailsDataSource.getPolyLine(origin, destination, wayPoints)
    }

    fun decodeWapPoints(stations: List<Station>): String {
        var points = ArrayList<LatLng>()
        for (i in 1..stations.size - 2) {
            val location = stations[i].location
            points.add(LatLng(location.lat, location.lng))
        }
        Log.e(TAG, "points:$points ")
        val encodedPoints = PolyUtil.encode(points)
        return "enc:${encodedPoints}:"
    }

    fun endTrip(tripId:String) {
        tripDetailsDataSource.endTrip(tripId)
    }

}