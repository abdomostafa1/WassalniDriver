package com.example.wassalniDR.database
import com.example.wassalniDR.data.TripDetails

import com.example.wassalniDR.datasource.DriverFinishedTripsResponse
import com.example.wassalniDR.datasource.DriverTripsResponse
import com.example.wassalniDR.datasource.driverRatingResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

@JvmSuppressWildcards
interface TripsRetrofit {
//    @GET("getAllTripsForDriver")
//    fun getAllTrips(@Header("token") token: String): Call<DriverTripsResponse>

    @GET("getAllUpcomingTripsForDriver")
    fun getUpComingTrips(@Header("token") token:String): Call<DriverTripsResponse>

    @GET("getAllPerviousTripsForDriver")
    fun getPerviousTrips(@Header("token") token:String) :Call<DriverFinishedTripsResponse>

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
    @GET("driverReview")
    fun getRating(
        @Header("token") token:String,
    ):Call<driverRatingResponse>

    @POST("addApologies")
    fun makeApology(@Header("token") token:String
    ,@Body body:Map<String,Any>):Call<Any>
}