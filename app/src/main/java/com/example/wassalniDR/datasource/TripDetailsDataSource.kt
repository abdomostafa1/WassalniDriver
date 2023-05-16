package com.example.wassalniDR.datasource

import android.util.Log
import com.example.wassalniDR.BuildConfig
import com.example.wassalniDR.data.LoggedInDriver
import com.example.wassalniDR.data.TripDetails
import com.example.wassalniDR.database.TripsRetrofit
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import java.lang.Exception

class TripDetailsDataSource @Inject constructor(
    private val tripService: TripsRetrofit,
    private val loggedInDriver: LoggedInDriver,
    private val directionApiService: DirectionApiService
) {
    private val TAG = "TripDetailsDataSource"

    suspend fun getTripDetails(id: String): TripDetails {
        val token = loggedInDriver.token
        val task = tripService.getTripDetails(token, id).execute()
        if (task.isSuccessful) {
            val tripDetails = task.body()!!
            Log.e(TAG, "getTripDetails: $tripDetails")
            return tripDetails
        }
        else
            throw Exception(task.errorBody()?.string())

    }

    suspend fun confirmArrival(tripId: String, stationIndex: Int) {
        val token = loggedInDriver.token
        val task = tripService.confirmStationArrival(token, tripId, stationIndex).execute()
        if (task.isSuccessful)
            Log.e(TAG, "Successful confirmArrival ")
        else
            throw Exception(task.errorBody()?.string())
    }

    fun recordPassengerAttendance(tripId: String, passengerTicket: Int) {
        val token = loggedInDriver.token
        val task = tripService.recordPassengerAttendance(token, tripId, passengerTicket).execute()
        if (task.isSuccessful)
            Log.e(TAG, "Successful Attendance ")
        else
            throw Exception(task.errorBody()?.string())
    }

    fun recordPassengerAbsence(tripId: String, passengerTicket: Int) {
        val token = loggedInDriver.token
        val task = tripService.recordPassengerAbsence(token, tripId, passengerTicket).execute()
        if (task.isSuccessful)
            Log.e(TAG, "Successful Absence ")
        else
            throw Exception(task.errorBody()?.string())
    }

    fun recordPassengerArrival(tripId: String, passengerTicket: Int) {
        val token = loggedInDriver.token
        val task = tripService.recordPassengerArrival(token, tripId, passengerTicket).execute()
        if (task.isSuccessful)
            Log.e(TAG, "Successful passenger Arrival ")
        else
            throw Exception(task.errorBody()?.string())
    }

    fun getPolyLine(origin: String, destination: String, waypoints: String): List<LatLng> {
        val key = BuildConfig.MAPS_API_KEY
        val task = directionApiService.getPolyLine1(origin, destination, waypoints, key).execute()
        if (task.isSuccessful) {
            Log.e(TAG, "getPolyLine: ${task.body()}")
            return handlePolyLineResponse(task.body()!!)
        } else
            throw Exception(task.errorBody()?.string())
    }

    private fun handlePolyLineResponse(response: String): List<LatLng> {
        val root = JSONObject(response)
        var points = emptyList<LatLng>()
        val route = root.getJSONArray("routes").getJSONObject(0)
        //val leg=route.getJSONArray("legs").getJSONObject(0)
        val overviewPolyline = route.getJSONObject("overview_polyline")
        val encodesPath = overviewPolyline.getString("points")

        points = PolyUtil.decode(encodesPath)
        Log.e(TAG, "points ${points.toString()}: ")
        Log.e(TAG, "encodesPath:$encodesPath")

        return points
        //val leg=route.getJSONArray("legs").getJSONObject(0)
    }

}

interface DirectionApiService {
    @GET("directions/json")
    fun getPolyLine1(
        @Query("origin", encoded = true) originLatLng: String,
        @Query("destination", encoded = true) destinationLatLng: String,
        @Query("waypoints", encoded = true) waypoints: String,
        @Query("key") key: String
    ): Call<String>

}