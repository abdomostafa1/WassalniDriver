package com.example.wassalniDR.data.uiState

sealed class TripFareUiState  {
    object InitialState:TripFareUiState()
    object Success:TripFareUiState()
    object Error:TripFareUiState()
    object Loading:TripFareUiState()
}