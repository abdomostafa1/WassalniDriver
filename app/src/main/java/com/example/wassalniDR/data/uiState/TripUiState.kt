package com.example.wassalniDR.data.uiState

import android.media.Rating

import com.example.wassalniDR.data.Trip

sealed class TripUiState{
    object Loading:TripUiState()
    data class Success(val trips: List<Trip>) : TripUiState()
    data class Error(val errorMsg: String) : TripUiState()
    object Empty:TripUiState()
}
