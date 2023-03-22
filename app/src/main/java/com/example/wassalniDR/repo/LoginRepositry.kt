package com.example.wassalniDR.repo

import com.example.wassalniDR.util.DriversRemoteDataSource

class LoginRepositry(private val driversRemoteDataSource: DriversRemoteDataSource) {

    val loginUiState=driversRemoteDataSource.loginUiState
    suspend fun makeLoginRequest(email: String, password: String) {
        val params=HashMap<String,Any>()
        params["email"]=email
        params["password"]=password
        driversRemoteDataSource.makeLoginRequest(params=params)
    }




}