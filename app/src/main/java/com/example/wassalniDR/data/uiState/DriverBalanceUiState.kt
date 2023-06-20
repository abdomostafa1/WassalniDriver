package com.example.wassalniDR.data.uiState

sealed class DriverBalanceUiState {

    object Loading :DriverBalanceUiState()

    data class Success(val balance: Int) : DriverBalanceUiState()

    data class Error(val errorMsg: String) : DriverBalanceUiState()

    object InitialState :DriverBalanceUiState()

}
