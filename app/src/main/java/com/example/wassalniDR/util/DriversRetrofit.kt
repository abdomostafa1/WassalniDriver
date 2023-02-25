package com.example.wassalniDR.util

import com.example.wassalniDR.data.Drivers
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

public interface DriversRetrofit {
    @POST("driver/create")
    fun createDriver(@Body driver: Drivers, @Header("app-id") appId: String): Call<Drivers>


}