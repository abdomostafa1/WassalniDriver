package com.example.wassalniDR.data

import com.squareup.moshi.Json


data class Trips(
    @Json(name = "_id")
    val id: String,
    val start: String,
    val destination: String,
    val startTime: Long,
    val endTime: Long,
    val price: Double
)

