package com.example.wassalniDR.data.uiState

import com.example.wassalniDR.data.Trips

sealed class TripUiState{
    object Loading:TripUiState()
    data class Success(val trips: List<Trips>) : TripUiState()
    data class Error(val errorMsg: String) : TripUiState()
    object Empty:TripUiState()
}
