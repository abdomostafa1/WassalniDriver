package com.example.wassalniDR.datasource

import com.example.wassalniDR.data.Driver
import com.example.wassalniDR.data.LoggedInDriver
import com.example.wassalniDR.database.TripsRetrofit
import org.json.JSONObject
import javax.inject.Inject

class BalanceDataSource @Inject constructor(
    private val remoteService: TripsRetrofit,
    private val loggedInDriver: LoggedInDriver
) {

    fun retrieveDriverData(): Driver {
        val token = loggedInDriver.token
        val request = remoteService.retrieveDriverData(token).execute()
        if (request.isSuccessful)
            return request.body()!!
        else {
            var errorMsg = ""
            request.errorBody()?.let { errorMsg = handleErrorMessage(it.string()) }
            throw Exception(errorMsg)
        }

    }

    private fun handleErrorMessage(json: String): String {
        val root = JSONObject(json)
        return root.getString("msg")
    }
}