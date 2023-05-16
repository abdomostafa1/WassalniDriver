package com.example.wassalniDR.data

import com.squareup.moshi.Json

data class Passenger(
    @Json(name = "_id")
    val id: String,
    val name: String,
    val point: Int,
    val numOfSeat: Int,
    val hasPaid: Boolean,
    @Json(name = "HasCome")
    var hasCome: Int,
    @Json (name="isArrived")
    var hasArrived: Boolean,
    val ticket:Int=5
)
