package com.example.wassalniDR.util

import com.example.wassalniDR.data.Drivers
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

public interface DriversRetrofit {
    @POST("signin")
    @JvmSuppressWildcards
    fun createDriver(@Body body:Map<String,Any>):Call<Map<String,Any>>
//    fun createDriver(@Body driver: Drivers): Call<Drivers>


}