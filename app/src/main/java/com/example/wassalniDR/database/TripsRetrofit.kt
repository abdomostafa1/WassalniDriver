package com.example.wassalniDR.database
import com.example.wassalniDR.data.TripDetails
import com.example.wassalniDR.data.Trip
import com.example.wassalniDR.datasource.DriverTripsResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.Call

@JvmSuppressWildcards
interface TripsRetrofit {
    @GET("getAllTripsForDriver")
    fun getAllTrips(@Header("token") token: String): Call<DriverTripsResponse>

    @GET("trip/{tripId}")
    fun getTripDetails(
        @Header("token") token: String,
        @Path("tripId") id: String
    ): Call<TripDetails>

    @GET("passenger/ShowUp/{tripId}/{passengerTicket}")
    fun recordPassengerAttendance(
        @Header("token") token: String,
        @Path("tripId") tripId: String,
        @Path("passengerTicket") ticket: Int
    ): Call<Any>

    @GET("passenger/DidNotshowedUp/{tripId}/{passengerTicket}")
    fun recordPassengerAbsence(
        @Header("token") token: String,
        @Path("tripId") tripId: String,
        @Path("passengerTicket") ticket: Int
    ): Call<Any>

    @GET("passenger/{tripId}/{passengerTicket}")
    fun recordPassengerArrival(
        @Header("token") token: String,
        @Path("tripId") tripId: String,
        @Path("passengerTicket") ticket: Int
    ): Call<Any>

    @GET("station/{tripId}/{stationIndex}")
    fun confirmStationArrival(
        @Header("token") token: String,
        @Path("tripId") tripId: String,
        @Path("stationIndex") index: Int
    ): Call<Any>
}