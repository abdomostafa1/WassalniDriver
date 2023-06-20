package com.example.wassalniDR.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wassalniDR.data.uiState.DriverBalanceUiState
import com.example.wassalniDR.repo.BalanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class BalanceViewModel @Inject constructor(private val balanceRepository: BalanceRepository) :
    ViewModel() {

    private val _balanceUiState =
        MutableStateFlow<DriverBalanceUiState>(DriverBalanceUiState.InitialState)
    val balanceUiState = _balanceUiState.asStateFlow()


    fun retrieveDriverBalance() {
        _balanceUiState.value = DriverBalanceUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val balanceResponse = balanceRepository.retrieveDriverBalance()
                _balanceUiState.value = DriverBalanceUiState.Success(balanceResponse.balance)
            }
            catch (e: Exception) {
                e.printStackTrace()
                if (e.message != null)
                    _balanceUiState.value = DriverBalanceUiState.Error(e.message!!)
            }
        }
    }

}