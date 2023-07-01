package com.example.wassalniDR.repo

import com.example.wassalniDR.data.Driver
import com.example.wassalniDR.datasource.BalanceDataSource
import javax.inject.Inject

class BalanceRepository @Inject constructor(private val balanceDataSource: BalanceDataSource){

    fun retrieveDriverData() : Driver {
        return balanceDataSource.retrieveDriverData()
    }
}