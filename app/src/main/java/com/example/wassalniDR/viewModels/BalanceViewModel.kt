package com.example.wassalniDR.viewModels

import androidx.lifecycle.ViewModel
import com.example.wassalniDR.data.Driver
import com.example.wassalniDR.repo.BalanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BalanceViewModel @Inject constructor(private val balanceRepository: BalanceRepository) :
    ViewModel() {

    fun retrieveDriverData() :Driver{
        return balanceRepository.retrieveDriverData()
    }

}