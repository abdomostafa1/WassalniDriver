package com.example.wassalniDR.repo

import com.example.wassalniDR.util.DriversRemoteDataSource
import javax.inject.Inject

class LoginRepository @Inject constructor(private val driversRemoteDataSource: DriversRemoteDataSource) {

    suspend fun makeLoginRequest(email: String, password: String) {
        val params=HashMap<String,Any>()
        params["email"]=email
        params["password"]=password
        driversRemoteDataSource.makeLoginRequest(params=params)
    }




}