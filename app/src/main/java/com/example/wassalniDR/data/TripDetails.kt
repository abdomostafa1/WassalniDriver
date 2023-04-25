package com.example.wassalniDR.data

data class TripDetails(
    val _id: String,
    val start: String,
    val destination: String,
    val startTime: Long,
    val endTime: Long,
    val price: Double,
    val availableSeats: Int=14,
    val driverId: String="kfhga45y6u7t",
    val driverName: String = "ahmed Nabil",
    val stations: List<Station>,
    val isDone: Boolean=false
)
data class Station(
    val _id: String,
    val isArrived: Boolean,
    val location: Location,
    val name: String,
    val time: Long,
    val rideStation: Boolean = true
)
data class Location(
    val lat: Double,
    val lng: Double
)