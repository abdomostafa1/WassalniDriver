package com.example.wassalniDR.database

import retrofit2.Call
import retrofit2.http.*

public interface DriversRetrofit {
    @POST("signin")
    @JvmSuppressWildcards
    fun loginDriver(@Body body:Map<String,Any>):Call<Map<String,Any>>




}