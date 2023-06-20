package com.example.wassalniDR.data

data class Rating(
        val _id: String,
        val message: String,
        val user: User,
        val driver: String,
        val ratingAverage: Int,
        val createdAt: String,
        val updatedAt: String,
        val __v: Int
)
data class User(
        val _id: String,
        val name: String
)
