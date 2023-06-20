package com.example.wassalniDR.datasource

import android.content.SharedPreferences
import com.example.wassalniDR.data.DriverBalanceResponse
import com.example.wassalniDR.database.TripsRetrofit
import javax.inject.Inject

class BalanceDataSource @Inject constructor(val remoteService:TripsRetrofit,val sharedPreference: SharedPreferences){

    fun retrieveDriverBalance() :DriverBalanceResponse{
        val token=sharedPreference.getString("token","")
        try {
            val request=remoteService.retrieveDriverBalance(token!!).execute()
            return request.body()!!
        }
        catch (e:Exception){
            throw e
        }
    }
}