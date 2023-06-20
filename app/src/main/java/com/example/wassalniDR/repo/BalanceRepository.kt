package com.example.wassalniDR.repo

import com.example.wassalniDR.data.DriverBalanceResponse
import com.example.wassalniDR.datasource.BalanceDataSource
import javax.inject.Inject

class BalanceRepository @Inject constructor(private val balanceDataSource: BalanceDataSource){

    fun retrieveDriverBalance() : DriverBalanceResponse {
        return balanceDataSource.retrieveDriverBalance()
    }
}