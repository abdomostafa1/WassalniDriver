package com.example.wassalniDR.data.uiState

sealed class ConfirmArrivalUiState {

    object InitialState:ConfirmArrivalUiState()

    object Loading:ConfirmArrivalUiState()

    object Success:ConfirmArrivalUiState()

    data class Error(val errorMsg:String):ConfirmArrivalUiState()
}
